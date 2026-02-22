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
      <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-navy-950 via-navy-900 to-navy-800">
        <div className="text-center animate-fade-in">
          <div className="relative">
            <div className="animate-spin rounded-full h-16 w-16 border-4 border-emerald-200 border-t-emerald-500 mx-auto"></div>
            <div className="absolute inset-0 rounded-full bg-emerald-500/20 blur-lg animate-pulse"></div>
          </div>
          <p className="mt-4 text-emerald-200 font-medium">Loading your portfolio...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-navy-950 via-navy-900 to-navy-800 animate-fade-in">
      {/* Background decoration */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-20 right-20 w-96 h-96 bg-emerald-500/5 rounded-full blur-3xl animate-pulse-slow"></div>
        <div className="absolute bottom-20 left-20 w-64 h-64 bg-navy-500/10 rounded-full blur-3xl animate-pulse-slow" style={{ animationDelay: '2s' }}></div>
      </div>

      <div className="relative z-10 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Welcome Section */}
        <div className="mb-8 animate-fade-in-up">
          <h1 className="text-5xl font-bold text-white mb-2">
            Welcome back, <span className="bg-gradient-to-r from-emerald-400 to-emerald-500 bg-clip-text text-transparent">{currentUser?.email?.split('@')[0]}</span>! 👋
          </h1>
          <p className="text-navy-200 text-xl">Continue your journey to master stock trading</p>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          {/* Portfolio Value */}
          <div className="glass-card rounded-xl p-6 border-l-4 border-emerald-500 card-hover animate-fade-in-up" style={{ animationDelay: '0.1s' }}>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-navy-300">Portfolio Value</p>
                <p className="text-3xl font-bold text-white mt-2">
                  ${portfolio?.totalValue.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) || '10,000.00'}
                </p>
                <p className="text-xs text-emerald-400 mt-1">+2.4% today</p>
              </div>
              <CurrencyDollarIcon className="h-12 w-12 text-emerald-400" />
            </div>
          </div>

          {/* Available Balance */}
          <div className="glass-card rounded-xl p-6 border-l-4 border-navy-500 card-hover animate-fade-in-up" style={{ animationDelay: '0.2s' }}>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-navy-300">Available Balance</p>
                <p className="text-3xl font-bold text-white mt-2">
                  ${portfolio?.balance.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) || '10,000.00'}
                </p>
                <p className="text-xs text-navy-400 mt-1">Ready to invest</p>
              </div>
              <CurrencyDollarIcon className="h-12 w-12 text-navy-400" />
            </div>
          </div>

          {/* Level */}
          <div className="glass-card rounded-xl p-6 border-l-4 border-emerald-600 card-hover animate-fade-in-up" style={{ animationDelay: '0.3s' }}>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-navy-300">Current Level</p>
                <p className="text-3xl font-bold text-white mt-2 capitalize">
                  {progress?.level || 'Beginner'}
                </p>
                <p className="text-xs text-emerald-400 mt-1">Keep learning!</p>
              </div>
              <AcademicCapIcon className="h-12 w-12 text-emerald-400" />
            </div>
          </div>

          {/* XP Points */}
          <div className="glass-card rounded-xl p-6 border-l-4 border-navy-600 card-hover animate-fade-in-up" style={{ animationDelay: '0.4s' }}>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-navy-300">Experience Points</p>
                <p className="text-3xl font-bold text-white mt-2">
                  {progress?.xp || 0} XP
                </p>
                <p className="text-xs text-emerald-400 mt-1">+50 this week</p>
              </div>
              <ChartBarIcon className="h-12 w-12 text-emerald-400" />
            </div>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <Link
            to="/trading"
            className="glass-card rounded-xl p-6 text-white hover:shadow-2xl smooth-transition group animate-fade-in-up card-hover"
            style={{ animationDelay: '0.5s' }}
          >
            <div className="flex items-center justify-between">
              <ChartBarIcon className="h-8 w-8 mb-4 text-emerald-400 group-hover:scale-110 smooth-transition" />
              <div className="w-12 h-12 bg-emerald-500/20 rounded-full flex items-center justify-center group-hover:bg-emerald-500/30 smooth-transition">
                <span className="text-emerald-400">📈</span>
              </div>
            </div>
            <h3 className="text-xl font-bold mb-2 group-hover:text-emerald-400 smooth-transition">Start Trading</h3>
            <p className="text-navy-300 group-hover:text-navy-200 smooth-transition">Practice with dummy currency and real market data</p>
          </Link>

          <Link
            to="/lessons"
            className="glass-card rounded-xl p-6 text-white hover:shadow-2xl smooth-transition group animate-fade-in-up card-hover"
            style={{ animationDelay: '0.6s' }}
          >
            <div className="flex items-center justify-between">
              <BookOpenIcon className="h-8 w-8 mb-4 text-emerald-400 group-hover:scale-110 smooth-transition" />
              <div className="w-12 h-12 bg-emerald-500/20 rounded-full flex items-center justify-center group-hover:bg-emerald-500/30 smooth-transition">
                <span className="text-emerald-400">📚</span>
              </div>
            </div>
            <h3 className="text-xl font-bold mb-2 group-hover:text-emerald-400 smooth-transition">Learn</h3>
            <p className="text-navy-300 group-hover:text-navy-200 smooth-transition">Explore interactive lessons and master the basics</p>
          </Link>

          <Link
            to="/ai-trainer"
            className="glass-card rounded-xl p-6 text-white hover:shadow-2xl smooth-transition group animate-fade-in-up card-hover"
            style={{ animationDelay: '0.7s' }}
          >
            <div className="flex items-center justify-between">
              <AcademicCapIcon className="h-8 w-8 mb-4 text-emerald-400 group-hover:scale-110 smooth-transition" />
              <div className="w-12 h-12 bg-emerald-500/20 rounded-full flex items-center justify-center group-hover:bg-emerald-500/30 smooth-transition">
                <span className="text-emerald-400">🤖</span>
              </div>
            </div>
            <h3 className="text-xl font-bold mb-2 group-hover:text-emerald-400 smooth-transition">AI Trainer</h3>
            <p className="text-navy-300 group-hover:text-navy-200 smooth-transition">Get personalized guidance and answer your questions</p>
          </Link>
        </div>

        {/* Top Stocks & Portfolio Holdings */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Top Stocks */}
          <div className="glass-card rounded-xl p-6 animate-fade-in-up card-hover" style={{ animationDelay: '0.8s' }}>
            <h2 className="text-2xl font-bold text-white mb-4 flex items-center">
              <span className="mr-3">📊</span>
              Top Stocks
            </h2>
            <div className="space-y-4">
              {topStocks.length > 0 ? (
                topStocks.map((stock, index) => (
                  <div key={stock.symbol} className="flex items-center justify-between p-4 bg-navy-800/30 rounded-lg smooth-transition hover:bg-navy-800/50 animate-fade-in-up" style={{ animationDelay: `${0.9 + index * 0.1}s` }}>
                    <div>
                      <p className="font-bold text-white">{stock.symbol}</p>
                      <p className="text-sm text-navy-300">{stock.name}</p>
                    </div>
                    <div className="text-right">
                      <p className="font-bold text-white">${stock.price.toFixed(2)}</p>
                      <div className={`flex items-center ${stock.change >= 0 ? 'text-emerald-400' : 'text-red-400'}`}>
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
                <p className="text-navy-400 text-center py-8">No stock data available</p>
              )}
            </div>
            <Link
              to="/trading"
              className="mt-6 block text-center text-emerald-400 hover:text-emerald-300 font-medium smooth-transition"
            >
              View All Stocks →
            </Link>
          </div>

          {/* Portfolio Holdings */}
          <div className="glass-card rounded-xl p-6 animate-fade-in-up card-hover" style={{ animationDelay: '0.8s' }}>
            <h2 className="text-2xl font-bold text-white mb-4 flex items-center">
              <span className="mr-3">💼</span>
              Your Holdings
            </h2>
            <div className="space-y-4">
              {portfolio?.holdings && portfolio.holdings.length > 0 ? (
                portfolio.holdings.map((holding, index) => (
                  <div key={holding.symbol} className="flex items-center justify-between p-4 bg-navy-800/30 rounded-lg smooth-transition hover:bg-navy-800/50 animate-fade-in-up" style={{ animationDelay: `${0.9 + index * 0.1}s` }}>
                    <div>
                      <p className="font-bold text-white">{holding.symbol}</p>
                      <p className="text-sm text-navy-300">{holding.quantity} shares</p>
                    </div>
                    <div className="text-right">
                      <p className="font-bold text-white">${holding.totalValue.toFixed(2)}</p>
                      <div className={`flex items-center ${holding.profit >= 0 ? 'text-emerald-400' : 'text-red-400'}`}>
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
                  <div className="text-6xl mb-4 animate-bounce-gentle">📈</div>
                  <p className="text-navy-400 mb-4">No holdings yet</p>
                  <Link
                    to="/trading"
                    className="inline-block px-6 py-3 bg-gradient-to-r from-emerald-500 to-emerald-600 text-white rounded-lg hover:from-emerald-600 hover:to-emerald-700 smooth-transition shadow-lg hover:shadow-xl"
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
          <div className="mt-8 glass-card rounded-xl p-6 animate-fade-in-up" style={{ animationDelay: '1.2s' }}>
            <h2 className="text-2xl font-bold text-white mb-4 flex items-center">
              <span className="mr-3">🎯</span>
              Your Progress
            </h2>
            <div className="space-y-4">
              <div>
                <div className="flex justify-between mb-2">
                  <span className="text-sm font-medium text-navy-300">Level Progress</span>
                  <span className="text-sm font-medium text-emerald-400">{progress.xp} XP</span>
                </div>
                <div className="w-full bg-navy-800/50 rounded-full h-4 overflow-hidden">
                  <div
                    className="bg-gradient-to-r from-emerald-400 to-emerald-500 h-4 rounded-full smooth-transition shadow-lg"
                    style={{ width: `${(progress.xp % 1000) / 10}%` }}
                  ></div>
                </div>
              </div>
              <div className="flex justify-between text-sm text-navy-300">
                <span>Completed Lessons: <span className="text-emerald-400 font-semibold">{progress.completedLessons.length}</span></span>
                <span>Rank: <span className="text-emerald-400 font-semibold">{progress.rank || 'Novice'}</span></span>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;



