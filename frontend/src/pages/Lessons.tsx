import React, { useEffect, useState, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { lessonApi, progressApi } from '../services/api';
import { Lesson, UserProgress } from '../services/api';
import { BookOpenIcon, CheckCircleIcon, LockClosedIcon } from '@heroicons/react/24/outline';
import toast from 'react-hot-toast';

const Lessons: React.FC = () => {
  const [lessons, setLessons] = useState<Lesson[]>([]);
  const [progress, setProgress] = useState<UserProgress | null>(null);
  const [selectedLevel, setSelectedLevel] = useState<'beginner' | 'intermediate' | 'advanced'>('beginner');
  const [loading, setLoading] = useState(true);

  const loadLessons = useCallback(async () => {
    try {
      setLoading(true);
      const data = await lessonApi.getLessons(selectedLevel);
      setLessons(data);
    } catch (error) {
      toast.error('Failed to load lessons');
    } finally {
      setLoading(false);
    }
  }, [selectedLevel]);

  const loadProgress = async () => {
    try {
      const data = await progressApi.getProgress();
      setProgress(data);
    } catch (error) {
      // Progress might not exist for new users
    }
  };

  useEffect(() => {
    loadLessons();
    loadProgress();
  }, [selectedLevel, loadLessons]);

  const isLessonCompleted = (lessonId: string) => {
    return progress?.completedLessons.includes(lessonId) || false;
  };

  const isLessonLocked = (lesson: Lesson) => {
    if (lesson.level === 'beginner') return false;
    if (lesson.level === 'intermediate') {
      return progress?.level === 'beginner';
    }
    if (lesson.level === 'advanced') {
      return progress?.level !== 'advanced';
    }
    return false;
  };

  const levels = [
    { id: 'beginner' as const, name: 'Beginner', color: 'bg-blue-500' },
    { id: 'intermediate' as const, name: 'Intermediate', color: 'bg-green-500' },
    { id: 'advanced' as const, name: 'Advanced', color: 'bg-purple-500' },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-2">Lessons</h1>
          <p className="text-gray-600">Master stock trading through interactive lessons</p>
        </div>

        {/* Level Tabs */}
        <div className="flex space-x-4 mb-8">
          {levels.map((level) => (
            <button
              key={level.id}
              onClick={() => setSelectedLevel(level.id)}
              className={`px-6 py-3 rounded-lg font-medium transition-colors ${
                selectedLevel === level.id
                  ? 'bg-gradient-to-r from-primary-600 to-success-600 text-white shadow-lg'
                  : 'bg-white text-gray-700 hover:bg-gray-100'
              }`}
            >
              {level.name}
            </button>
          ))}
        </div>

        {/* Lessons Grid */}
        {loading ? (
          <div className="flex items-center justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {lessons.map((lesson, index) => {
              const completed = isLessonCompleted(lesson.id);
              const locked = isLessonLocked(lesson);

              return (
                <div
                  key={lesson.id}
                  className={`bg-white rounded-xl shadow-lg p-6 transition-transform hover:scale-105 ${
                    locked ? 'opacity-60' : ''
                  }`}
                >
                  <div className="flex items-start justify-between mb-4">
                    <div className={`${levels.find(l => l.id === lesson.level)?.color || 'bg-gray-500'} text-white px-3 py-1 rounded-full text-sm font-medium`}>
                      Lesson {index + 1}
                    </div>
                    {completed ? (
                      <CheckCircleIcon className="h-6 w-6 text-success-600" />
                    ) : locked ? (
                      <LockClosedIcon className="h-6 w-6 text-gray-400" />
                    ) : (
                      <BookOpenIcon className="h-6 w-6 text-primary-600" />
                    )}
                  </div>

                  <h3 className="text-xl font-bold text-gray-900 mb-2">{lesson.title}</h3>
                  <p className="text-gray-600 mb-4">{lesson.description}</p>

                  <div className="flex items-center justify-between">
                    <span className="text-sm text-gray-500">
                      {lesson.questions.length} questions
                    </span>
                    {locked ? (
                      <span className="text-sm text-gray-500">Locked</span>
                    ) : (
                      <Link
                        to={`/lessons/${lesson.id}`}
                        className="px-4 py-2 bg-gradient-to-r from-primary-600 to-success-600 text-white rounded-lg hover:shadow-lg transition-shadow"
                      >
                        {completed ? 'Review' : 'Start'}
                      </Link>
                    )}
                  </div>
                </div>
              );
            })}
          </div>
        )}

        {!loading && lessons.length === 0 && (
          <div className="text-center py-12">
            <p className="text-gray-500 text-lg">No lessons available for this level yet.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Lessons;



