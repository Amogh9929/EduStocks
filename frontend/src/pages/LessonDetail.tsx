import React, { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { lessonApi } from '../services/api';
import { Lesson } from '../services/api';
import { CheckCircleIcon, XCircleIcon } from '@heroicons/react/24/outline';
import toast from 'react-hot-toast';

const LessonDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [lesson, setLesson] = useState<Lesson | null>(null);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [selectedAnswer, setSelectedAnswer] = useState<number | null>(null);
  const [showExplanation, setShowExplanation] = useState(false);
  const [score, setScore] = useState(0);
  const [completed, setCompleted] = useState(false);
  const [loading, setLoading] = useState(true);

  const loadLesson = useCallback(async () => {
    if (!id) return;
    try {
      setLoading(true);
      const data = await lessonApi.getLesson(id);
      setLesson(data);
    } catch (error) {
      toast.error('Failed to load lesson');
      navigate('/lessons');
    } finally {
      setLoading(false);
    }
  }, [id, navigate]);

  useEffect(() => {
    loadLesson();
  }, [id, loadLesson]);

  const handleAnswerSelect = (answerIndex: number) => {
    if (showExplanation) return;
    setSelectedAnswer(answerIndex);
  };

  const handleSubmitAnswer = () => {
    if (selectedAnswer === null) return;

    const currentQuestion = lesson?.questions[currentQuestionIndex];
    if (!currentQuestion) return;

    const isCorrect = selectedAnswer === currentQuestion.correctAnswer;
    if (isCorrect) {
      setScore(score + 1);
      toast.success('Correct!');
    } else {
      toast.error('Incorrect. Check the explanation.');
    }

    setShowExplanation(true);
  };

  const handleNextQuestion = () => {
    if (currentQuestionIndex < (lesson?.questions.length || 0) - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
      setSelectedAnswer(null);
      setShowExplanation(false);
    } else {
      handleCompleteLesson();
    }
  };

  const handleCompleteLesson = async () => {
    if (!lesson || !id) return;

    try {
      const finalScore = (score / lesson.questions.length) * 100;
      await lessonApi.completeLesson(id, finalScore);
      setCompleted(true);
      toast.success(`Lesson completed! Score: ${finalScore.toFixed(0)}%`);
    } catch (error) {
      toast.error('Failed to complete lesson');
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  if (!lesson) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-gray-500">Lesson not found</p>
      </div>
    );
  }

  const currentQuestion = lesson.questions[currentQuestionIndex];

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Lesson Header */}
        <div className="mb-8">
          <button
            onClick={() => navigate('/lessons')}
            className="text-primary-600 hover:text-primary-700 mb-4"
          >
            ← Back to Lessons
          </button>
          <h1 className="text-4xl font-bold text-gray-900 mb-2">{lesson.title}</h1>
          <p className="text-gray-600">{lesson.description}</p>
        </div>

        {/* Lesson Content */}
        {!completed && currentQuestionIndex === 0 && (
          <div className="bg-white rounded-xl shadow-lg p-6 mb-6">
            <div className="prose max-w-none" dangerouslySetInnerHTML={{ __html: lesson.content }} />
          </div>
        )}

        {/* Question Section */}
        {!completed && currentQuestion && (
          <div className="bg-white rounded-xl shadow-lg p-6 mb-6">
            <div className="mb-4">
              <div className="flex justify-between items-center mb-4">
                <span className="text-sm font-medium text-gray-600">
                  Question {currentQuestionIndex + 1} of {lesson.questions.length}
                </span>
                <span className="text-sm font-medium text-gray-600">
                  Score: {score}/{lesson.questions.length}
                </span>
              </div>
              <h2 className="text-2xl font-bold text-gray-900">{currentQuestion.question}</h2>
            </div>

            <div className="space-y-3 mb-6">
              {currentQuestion.options.map((option, index) => {
                const isSelected = selectedAnswer === index;
                const isCorrect = index === currentQuestion.correctAnswer;
                const showResult = showExplanation;

                let bgColor = 'bg-gray-50 hover:bg-gray-100';
                if (showResult) {
                  if (isCorrect) {
                    bgColor = 'bg-success-50 border-2 border-success-600';
                  } else if (isSelected && !isCorrect) {
                    bgColor = 'bg-red-50 border-2 border-red-600';
                  }
                } else if (isSelected) {
                  bgColor = 'bg-primary-50 border-2 border-primary-600';
                }

                return (
                  <button
                    key={index}
                    onClick={() => handleAnswerSelect(index)}
                    disabled={showExplanation}
                    className={`w-full text-left p-4 rounded-lg border-2 border-transparent transition-colors ${bgColor} ${
                      !showExplanation ? 'cursor-pointer' : 'cursor-default'
                    }`}
                  >
                    <div className="flex items-center justify-between">
                      <span>{option}</span>
                      {showResult && isCorrect && (
                        <CheckCircleIcon className="h-6 w-6 text-success-600" />
                      )}
                      {showResult && isSelected && !isCorrect && (
                        <XCircleIcon className="h-6 w-6 text-red-600" />
                      )}
                    </div>
                  </button>
                );
              })}
            </div>

            {showExplanation && (
              <div className="mb-6 p-4 bg-blue-50 rounded-lg border-l-4 border-primary-600">
                <p className="font-medium text-gray-900 mb-2">Explanation:</p>
                <p className="text-gray-700">{currentQuestion.explanation}</p>
              </div>
            )}

            <div className="flex justify-end">
              {!showExplanation ? (
                <button
                  onClick={handleSubmitAnswer}
                  disabled={selectedAnswer === null}
                  className="px-6 py-2 bg-gradient-to-r from-primary-600 to-success-600 text-white rounded-lg hover:shadow-lg disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Submit Answer
                </button>
              ) : (
                <button
                  onClick={handleNextQuestion}
                  className="px-6 py-2 bg-gradient-to-r from-primary-600 to-success-600 text-white rounded-lg hover:shadow-lg"
                >
                  {currentQuestionIndex < lesson.questions.length - 1 ? 'Next Question' : 'Complete Lesson'}
                </button>
              )}
            </div>
          </div>
        )}

        {/* Completion Screen */}
        {completed && (
          <div className="bg-white rounded-xl shadow-lg p-8 text-center">
            <CheckCircleIcon className="h-16 w-16 text-success-600 mx-auto mb-4" />
            <h2 className="text-3xl font-bold text-gray-900 mb-2">Lesson Completed!</h2>
            <p className="text-xl text-gray-600 mb-6">
              Your score: {((score / lesson.questions.length) * 100).toFixed(0)}%
            </p>
            <div className="flex space-x-4 justify-center">
              <button
                onClick={() => navigate('/lessons')}
                className="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300"
              >
                Back to Lessons
              </button>
              <button
                onClick={() => navigate('/ai-trainer')}
                className="px-6 py-2 bg-gradient-to-r from-primary-600 to-success-600 text-white rounded-lg hover:shadow-lg"
              >
                Try AI Trainer
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default LessonDetail;



