import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { portfolioApi, progressApi, stockApi } from '../services/api';
import { Portfolio, UserProgress, Stock } from '../services/api';
import {
  ChartBarIcon,
  BookOpenIcon,
  AcademicCapIcon,
  CurrencyDollarIcon,
  ArrowTrendingUpIcon,
  ArrowTrendingDownIcon,
} from '@heroicons/react/24/outline';
import toast from 'react-hot-toast';

const Dashboard: React.FC = () => {
  const { currentUser } = useAuth();
  const [portfolio, setPortfolio] = useState<Portfolio | null>(null);
  const [progress, setProgress] = useState<UserProgress | null>(null);
  const [topStocks, setTopStocks] = useState<Stock[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      const [portfolioData, progressData, stocksData] = await Promise.all([
        portfolioApi.getPortfolio().catch(() => null),
        progressApi.getProgress().catch(() => null),
        stockApi.getStocks().catch(() => []),
      ]);

      setPortfolio(portfolioData);
      setProgress(progressData);
      setTopStocks(stocksData.slice(0, 5));
    } catch (error) {
      toast.error('Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
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
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Welcome Section */}
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-2">
            Welcome back, {currentUser?.email?.split('@')[0]}! 👋
          </h1>
          <p className="text-gray-600">Continue your journey to master stock trading</p>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          {/* Portfolio Value */}
          <div className="bg-white rounded-xl shadow-lg p-6 border-l-4 border-primary-600">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Portfolio Value</p>
                <p className="text-2xl font-bold text-gray-900 mt-2">
                  ${portfolio?.totalValue.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) || '10,000.00'}
                </p>
              </div>
              <CurrencyDollarIcon className="h-12 w-12 text-primary-600" />
            </div>
          </div>

          {/* Available Balance */}
          <div className="bg-white rounded-xl shadow-lg p-6 border-l-4 border-success-600">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Available Balance</p>
                <p className="text-2xl font-bold text-gray-900 mt-2">
                  ${portfolio?.balance.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) || '10,000.00'}
                </p>
              </div>
              <CurrencyDollarIcon className="h-12 w-12 text-success-600" />
            </div>
          </div>

          {/* Level */}
          <div className="bg-white rounded-xl shadow-lg p-6 border-l-4 border-primary-500">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Current Level</p>
                <p className="text-2xl font-bold text-gray-900 mt-2 capitalize">
                  {progress?.level || 'Beginner'}
                </p>
              </div>
              <AcademicCapIcon className="h-12 w-12 text-primary-500" />
            </div>
          </div>

          {/* XP Points */}
          <div className="bg-white rounded-xl shadow-lg p-6 border-l-4 border-success-500">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Experience Points</p>
                <p className="text-2xl font-bold text-gray-900 mt-2">
                  {progress?.xp || 0} XP
                </p>
              </div>
              <ChartBarIcon className="h-12 w-12 text-success-500" />
            </div>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <Link
            to="/trading"
            className="bg-gradient-to-r from-primary-600 to-primary-700 rounded-xl shadow-lg p-6 text-white hover:shadow-xl transition-shadow"
          >
            <ChartBarIcon className="h-8 w-8 mb-4" />
            <h3 className="text-xl font-bold mb-2">Start Trading</h3>
            <p className="text-primary-100">Practice with dummy currency and real market data</p>
          </Link>

          <Link
            to="/lessons"
            className="bg-gradient-to-r from-success-600 to-success-700 rounded-xl shadow-lg p-6 text-white hover:shadow-xl transition-shadow"
          >
            <BookOpenIcon className="h-8 w-8 mb-4" />
            <h3 className="text-xl font-bold mb-2">Learn</h3>
            <p className="text-success-100">Explore interactive lessons and master the basics</p>
          </Link>

          <Link
            to="/ai-trainer"
            className="bg-gradient-to-r from-primary-500 to-success-500 rounded-xl shadow-lg p-6 text-white hover:shadow-xl transition-shadow"
          >
            <AcademicCapIcon className="h-8 w-8 mb-4" />
            <h3 className="text-xl font-bold mb-2">AI Trainer</h3>
            <p className="text-white/90">Get personalized guidance and answer your questions</p>
          </Link>
        </div>

        {/* Top Stocks & Portfolio Holdings */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Top Stocks */}
          <div className="bg-white rounded-xl shadow-lg p-6">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">Top Stocks</h2>
            <div className="space-y-4">
              {topStocks.length > 0 ? (
                topStocks.map((stock) => (
                  <div key={stock.symbol} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                    <div>
                      <p className="font-bold text-gray-900">{stock.symbol}</p>
                      <p className="text-sm text-gray-600">{stock.name}</p>
                    </div>
                    <div className="text-right">
                      <p className="font-bold text-gray-900">${stock.price.toFixed(2)}</p>
                      <div className={`flex items-center ${stock.change >= 0 ? 'text-success-600' : 'text-red-600'}`}>
                        {stock.change >= 0 ? (
                          <ArrowTrendingUpIcon className="h-4 w-4 mr-1" />
                        ) : (
                          <ArrowTrendingDownIcon className="h-4 w-4 mr-1" />
                        )}
                        <span className="text-sm">{stock.changePercent.toFixed(2)}%</span>
                      </div>
                    </div>
                  </div>
                ))
              ) : (
                <p className="text-gray-500 text-center py-4">No stock data available</p>
              )}
            </div>
            <Link
              to="/trading"
              className="mt-4 block text-center text-primary-600 hover:text-primary-700 font-medium"
            >
              View All Stocks →
            </Link>
          </div>

          {/* Portfolio Holdings */}
          <div className="bg-white rounded-xl shadow-lg p-6">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">Your Holdings</h2>
            <div className="space-y-4">
              {portfolio?.holdings && portfolio.holdings.length > 0 ? (
                portfolio.holdings.map((holding) => (
                  <div key={holding.symbol} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                    <div>
                      <p className="font-bold text-gray-900">{holding.symbol}</p>
                      <p className="text-sm text-gray-600">{holding.quantity} shares</p>
                    </div>
                    <div className="text-right">
                      <p className="font-bold text-gray-900">${holding.totalValue.toFixed(2)}</p>
                      <div className={`flex items-center ${holding.profit >= 0 ? 'text-success-600' : 'text-red-600'}`}>
                        {holding.profit >= 0 ? (
                          <ArrowTrendingUpIcon className="h-4 w-4 mr-1" />
                        ) : (
                          <ArrowTrendingDownIcon className="h-4 w-4 mr-1" />
                        )}
                        <span className="text-sm">{holding.profitPercent.toFixed(2)}%</span>
                      </div>
                    </div>
                  </div>
                ))
              ) : (
                <div className="text-center py-8">
                  <p className="text-gray-500 mb-4">No holdings yet</p>
                  <Link
                    to="/trading"
                    className="inline-block px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700"
                  >
                    Start Trading
                  </Link>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Progress Section */}
        {progress && (
          <div className="mt-8 bg-white rounded-xl shadow-lg p-6">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">Your Progress</h2>
            <div className="space-y-4">
              <div>
                <div className="flex justify-between mb-2">
                  <span className="text-sm font-medium text-gray-700">Level Progress</span>
                  <span className="text-sm font-medium text-gray-700">{progress.xp} XP</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-3">
                  <div
                    className="bg-gradient-to-r from-primary-600 to-success-600 h-3 rounded-full transition-all"
                    style={{ width: `${(progress.xp % 1000) / 10}%` }}
                  ></div>
                </div>
              </div>
              <div className="flex justify-between text-sm text-gray-600">
                <span>Completed Lessons: {progress.completedLessons.length}</span>
                <span>Rank: {progress.rank || 'Novice'}</span>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;



