import React, { useEffect, useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { progressApi, portfolioApi } from '../services/api';
import { UserProgress, Portfolio } from '../services/api';
import { AcademicCapIcon, ChartBarIcon, TrophyIcon } from '@heroicons/react/24/outline';
import toast from 'react-hot-toast';

const Profile: React.FC = () => {
  const { currentUser } = useAuth();
  const [progress, setProgress] = useState<UserProgress | null>(null);
  const [portfolio, setPortfolio] = useState<Portfolio | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadProfileData();
  }, []);

  const loadProfileData = async () => {
    try {
      const [progressData, portfolioData] = await Promise.all([
        progressApi.getProgress().catch(() => null),
        portfolioApi.getPortfolio().catch(() => null),
      ]);
      setProgress(progressData);
      setPortfolio(portfolioData);
    } catch (error) {
      toast.error('Failed to load profile data');
    } finally {
      setLoading(false);
    }
  };

  const getRank = (xp: number) => {
    if (xp < 100) return 'Novice';
    if (xp < 500) return 'Beginner';
    if (xp < 1000) return 'Intermediate';
    if (xp < 2500) return 'Advanced';
    if (xp < 5000) return 'Expert';
    return 'Master';
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-2">Profile</h1>
          <p className="text-gray-600">Your learning journey and achievements</p>
        </div>

        {/* User Info Card */}
        <div className="bg-white rounded-xl shadow-lg p-6 mb-6">
          <div className="flex items-center space-x-4">
            <div className="h-20 w-20 rounded-full bg-gradient-to-r from-primary-600 to-success-600 flex items-center justify-center text-white text-2xl font-bold">
              {currentUser?.email?.charAt(0).toUpperCase()}
            </div>
            <div>
              <h2 className="text-2xl font-bold text-gray-900">{currentUser?.email}</h2>
              <p className="text-gray-600">Student Investor</p>
            </div>
          </div>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
          <div className="bg-white rounded-xl shadow-lg p-6 border-l-4 border-primary-600">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Level</p>
                <p className="text-2xl font-bold text-gray-900 mt-2 capitalize">
                  {progress?.level || 'Beginner'}
                </p>
              </div>
              <AcademicCapIcon className="h-12 w-12 text-primary-600" />
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-lg p-6 border-l-4 border-success-600">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Experience Points</p>
                <p className="text-2xl font-bold text-gray-900 mt-2">
                  {progress?.xp || 0} XP
                </p>
              </div>
              <ChartBarIcon className="h-12 w-12 text-success-600" />
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-lg p-6 border-l-4 border-yellow-500">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Rank</p>
                <p className="text-2xl font-bold text-gray-900 mt-2">
                  {getRank(progress?.xp || 0)}
                </p>
              </div>
              <TrophyIcon className="h-12 w-12 text-yellow-500" />
            </div>
          </div>
        </div>

        {/* Progress Section */}
        <div className="bg-white rounded-xl shadow-lg p-6 mb-6">
          <h3 className="text-xl font-bold text-gray-900 mb-4">Learning Progress</h3>
          <div className="space-y-4">
            <div>
              <div className="flex justify-between mb-2">
                <span className="text-sm font-medium text-gray-700">Level Progress</span>
                <span className="text-sm font-medium text-gray-700">
                  {(progress?.xp || 0) % 1000}/1000 XP
                </span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-4">
                <div
                  className="bg-gradient-to-r from-primary-600 to-success-600 h-4 rounded-full transition-all"
                  style={{ width: `${((progress?.xp || 0) % 1000) / 10}%` }}
                ></div>
              </div>
            </div>
            <div className="flex justify-between text-sm text-gray-600">
              <span>Completed Lessons: {progress?.completedLessons.length || 0}</span>
              <span>Next Level: {1000 - ((progress?.xp || 0) % 1000)} XP</span>
            </div>
          </div>
        </div>

        {/* Portfolio Summary */}
        <div className="bg-white rounded-xl shadow-lg p-6 mb-6">
          <h3 className="text-xl font-bold text-gray-900 mb-4">Portfolio Summary</h3>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <p className="text-sm text-gray-600">Total Value</p>
              <p className="text-2xl font-bold text-gray-900">
                ${portfolio?.totalValue.toFixed(2) || '0.00'}
              </p>
            </div>
            <div>
              <p className="text-sm text-gray-600">Available Balance</p>
              <p className="text-2xl font-bold text-gray-900">
                ${portfolio?.balance.toFixed(2) || '0.00'}
              </p>
            </div>
            <div>
              <p className="text-sm text-gray-600">Total Holdings</p>
              <p className="text-2xl font-bold text-gray-900">
                {portfolio?.holdings.length || 0}
              </p>
            </div>
            <div>
              <p className="text-sm text-gray-600">Invested Amount</p>
              <p className="text-2xl font-bold text-gray-900">
                ${((portfolio?.totalValue || 0) - (portfolio?.balance || 0)).toFixed(2)}
              </p>
            </div>
          </div>
        </div>

        {/* Achievements */}
        <div className="bg-white rounded-xl shadow-lg p-6">
          <h3 className="text-xl font-bold text-gray-900 mb-4">Achievements</h3>
          <div className="space-y-2">
            <div className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
              <TrophyIcon className="h-6 w-6 text-yellow-500" />
              <div>
                <p className="font-medium text-gray-900">First Trade</p>
                <p className="text-sm text-gray-600">Complete your first stock trade</p>
              </div>
            </div>
            <div className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
              <AcademicCapIcon className="h-6 w-6 text-blue-500" />
              <div>
                <p className="font-medium text-gray-900">First Lesson</p>
                <p className="text-sm text-gray-600">Complete your first lesson</p>
              </div>
            </div>
            {(progress?.completedLessons.length || 0) >= 5 && (
              <div className="flex items-center space-x-3 p-3 bg-green-50 rounded-lg border-2 border-success-600">
                <TrophyIcon className="h-6 w-6 text-success-600" />
                <div>
                  <p className="font-medium text-gray-900">Knowledge Seeker</p>
                  <p className="text-sm text-gray-600">Complete 5 lessons</p>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;



