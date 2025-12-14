import React, { useState } from 'react';
import { aiTrainerApi, progressApi } from '../services/api';
import { AITrainerQuestion } from '../services/api';
import { AcademicCapIcon, LightBulbIcon, ChatBubbleLeftRightIcon } from '@heroicons/react/24/outline';
import toast from 'react-hot-toast';

const AITrainer: React.FC = () => {
  const [question, setQuestion] = useState<AITrainerQuestion | null>(null);
  const [selectedAnswer, setSelectedAnswer] = useState<number | null>(null);
  const [showExplanation, setShowExplanation] = useState(false);
  const [userQuery, setUserQuery] = useState('');
  const [aiResponse, setAiResponse] = useState('');
  const [loading, setLoading] = useState(false);
  const [queryLoading, setQueryLoading] = useState(false);
  const [level, setLevel] = useState<'beginner' | 'intermediate' | 'advanced'>('beginner');

  React.useEffect(() => {
    loadProgress();
  }, []);

  const loadProgress = async () => {
    try {
      const data = await progressApi.getProgress();
      if (data) {
        setLevel(data.level);
      }
    } catch (error) {
      // Progress might not exist
    }
  };

  const getQuestion = async () => {
    try {
      setLoading(true);
      setShowExplanation(false);
      setSelectedAnswer(null);
      const data = await aiTrainerApi.getQuestion(level);
      setQuestion(data);
      setAiResponse('');
    } catch (error) {
      toast.error('Failed to load question');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmitQuery = async () => {
    if (!userQuery.trim()) {
      toast.error('Please enter a question');
      return;
    }

    try {
      setQueryLoading(true);
      const response = await aiTrainerApi.askQuestion(userQuery, level);
      setAiResponse(response);
      setUserQuery('');
    } catch (error) {
      toast.error('Failed to get AI response');
      console.error(error);
    } finally {
      setQueryLoading(false);
    }
  };

  const handleAnswerSelect = (answerIndex: number) => {
    if (showExplanation) return;
    setSelectedAnswer(answerIndex);
  };

  const handleSubmitAnswer = async () => {
    if (selectedAnswer === null || !question) return;

    try {
      const response = await aiTrainerApi.submitAnswer(question.question, selectedAnswer);
      setShowExplanation(true);
      if (response.correct) {
        toast.success('Correct! Great job!');
      } else {
        toast.error('Incorrect. Check the explanation.');
      }
    } catch (error) {
      toast.error('Failed to submit answer');
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-2 flex items-center">
            <AcademicCapIcon className="h-10 w-10 mr-3 text-primary-600" />
            AI Trainer
          </h1>
          <p className="text-gray-600">Get personalized guidance and answers to your stock market questions</p>
        </div>

        {/* Level Selection */}
        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-700 mb-2">Select Level</label>
          <div className="flex space-x-4">
            {['beginner', 'intermediate', 'advanced'].map((lvl) => (
              <button
                key={lvl}
                onClick={() => setLevel(lvl as 'beginner' | 'intermediate' | 'advanced')}
                className={`px-4 py-2 rounded-lg font-medium capitalize ${
                  level === lvl
                    ? 'bg-gradient-to-r from-primary-600 to-success-600 text-white'
                    : 'bg-white text-gray-700 hover:bg-gray-100'
                }`}
              >
                {lvl}
              </button>
            ))}
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Practice Questions */}
          <div className="bg-white rounded-xl shadow-lg p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-2xl font-bold text-gray-900 flex items-center">
                <LightBulbIcon className="h-6 w-6 mr-2 text-yellow-500" />
                Practice Questions
              </h2>
              <button
                onClick={getQuestion}
                disabled={loading}
                className="px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 disabled:opacity-50"
              >
                {loading ? 'Loading...' : 'New Question'}
              </button>
            </div>

            {question ? (
              <div>
                <div className="mb-4">
                  <span className="text-xs font-medium text-gray-500 bg-gray-100 px-2 py-1 rounded">
                    {question.topic}
                  </span>
                </div>
                <h3 className="text-lg font-bold text-gray-900 mb-4">{question.question}</h3>

                <div className="space-y-3 mb-6">
                  {question.options.map((option, index) => {
                    const isSelected = selectedAnswer === index;
                    return (
                      <button
                        key={index}
                        onClick={() => handleAnswerSelect(index)}
                        disabled={showExplanation}
                        className={`w-full text-left p-4 rounded-lg border-2 transition-colors ${
                          isSelected
                            ? 'bg-primary-50 border-primary-600'
                            : 'bg-gray-50 border-transparent hover:bg-gray-100'
                        } ${showExplanation ? 'cursor-default' : 'cursor-pointer'}`}
                      >
                        {option}
                      </button>
                    );
                  })}
                </div>

                {showExplanation && (
                  <div className="mb-6 p-4 bg-blue-50 rounded-lg border-l-4 border-primary-600">
                    <p className="font-medium text-gray-900 mb-2">Explanation:</p>
                    <p className="text-gray-700">{question.explanation}</p>
                  </div>
                )}

                {!showExplanation && (
                  <button
                    onClick={handleSubmitAnswer}
                    disabled={selectedAnswer === null}
                    className="w-full px-6 py-2 bg-gradient-to-r from-primary-600 to-success-600 text-white rounded-lg hover:shadow-lg disabled:opacity-50"
                  >
                    Submit Answer
                  </button>
                )}

                {showExplanation && (
                  <button
                    onClick={getQuestion}
                    className="w-full px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300"
                  >
                    Next Question
                  </button>
                )}
              </div>
            ) : (
              <div className="text-center py-12">
                <p className="text-gray-500 mb-4">Click "New Question" to start practicing</p>
                <LightBulbIcon className="h-16 w-16 text-gray-300 mx-auto" />
              </div>
            )}
          </div>

          {/* Ask AI */}
          <div className="bg-white rounded-xl shadow-lg p-6">
            <h2 className="text-2xl font-bold text-gray-900 mb-4 flex items-center">
              <ChatBubbleLeftRightIcon className="h-6 w-6 mr-2 text-green-500" />
              Ask AI Trainer
            </h2>
            <p className="text-gray-600 mb-4">
              Ask any question about stocks, trading, or market concepts. Our AI will help you understand!
            </p>

            <div className="mb-4">
              <textarea
                value={userQuery}
                onChange={(e) => setUserQuery(e.target.value)}
                placeholder="e.g., What is a stock split? How does dividend work? Explain P/E ratio..."
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent resize-none"
                rows={4}
                onKeyDown={(e) => {
                  if (e.key === 'Enter' && e.ctrlKey) {
                    handleSubmitQuery();
                  }
                }}
              />
            </div>

            <button
              onClick={handleSubmitQuery}
              disabled={queryLoading || !userQuery.trim()}
              className="w-full px-6 py-2 bg-gradient-to-r from-success-600 to-success-700 text-white rounded-lg hover:shadow-lg disabled:opacity-50 mb-4"
            >
              {queryLoading ? 'Thinking...' : 'Ask AI (Ctrl+Enter)'}
            </button>

            {aiResponse && (
              <div className="mt-4 p-4 bg-green-50 rounded-lg border-l-4 border-success-600">
                <p className="font-medium text-gray-900 mb-2">AI Response:</p>
                <p className="text-gray-700 whitespace-pre-wrap">{aiResponse}</p>
              </div>
            )}

            <div className="mt-4 p-4 bg-blue-50 rounded-lg">
              <p className="text-sm text-gray-600">
                <strong>Tip:</strong> Ask about stock concepts, trading strategies, market analysis, or get explanations for complex terms.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AITrainer;



