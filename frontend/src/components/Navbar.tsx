import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import {
  HomeIcon,
  BookOpenIcon,
  ChartBarIcon,
  AcademicCapIcon,
  UserIcon,
  ArrowRightOnRectangleIcon,
} from '@heroicons/react/24/outline';

const Navbar: React.FC = () => {
  const { currentUser, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/login');
    } catch (error) {
      console.error('Failed to log out:', error);
    }
  };

  const navigation = [
    { name: 'Dashboard', href: '/', icon: HomeIcon },
    { name: 'Lessons', href: '/lessons', icon: BookOpenIcon },
    { name: 'Trading', href: '/trading', icon: ChartBarIcon },
    { name: 'AI Trainer', href: '/ai-trainer', icon: AcademicCapIcon },
    { name: 'Profile', href: '/profile', icon: UserIcon },
  ];

  const isActiveRoute = (href: string) => {
    if (href === '/') {
      return location.pathname === '/';
    }
    return location.pathname.startsWith(href);
  };

  return (
    <nav className="glass-card border-b border-navy-700/30 backdrop-blur-xl sticky top-0 z-50 animate-fade-in">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex">
            <div className="flex-shrink-0 flex items-center">
              <Link 
                to="/" 
                className="text-2xl font-bold bg-gradient-to-r from-emerald-400 to-emerald-500 bg-clip-text text-transparent hover:from-emerald-300 hover:to-emerald-400 smooth-transition group"
              >
                <span className="group-hover:scale-110 inline-block smooth-transition">📚</span> EduStocks
              </Link>
            </div>
            <div className="hidden sm:ml-8 sm:flex sm:space-x-2">
              {navigation.map((item) => {
                const Icon = item.icon;
                const isActive = isActiveRoute(item.href);
                return (
                  <Link
                    key={item.name}
                    to={item.href}
                    className={`inline-flex items-center px-4 py-2 rounded-lg text-sm font-medium smooth-transition group ${
                      isActive
                        ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30'
                        : 'text-navy-300 hover:text-white hover:bg-navy-800/50'
                    }`}
                  >
                    <Icon className={`h-5 w-5 mr-2 group-hover:scale-110 smooth-transition ${
                      isActive ? 'text-emerald-400' : ''
                    }`} />
                    {item.name}
                  </Link>
                );
              })}
            </div>
          </div>
          <div className="flex items-center space-x-4">
            <div className="hidden sm:block">
              <span className="text-sm text-navy-300 bg-navy-800/30 px-3 py-1 rounded-full border border-navy-700/30">
                {currentUser?.email?.split('@')[0]}
              </span>
            </div>
            <button
              onClick={handleLogout}
              className="inline-flex items-center px-4 py-2 border border-emerald-600 text-sm font-medium rounded-lg text-emerald-400 bg-emerald-500/10 hover:bg-emerald-500/20 hover:text-emerald-300 smooth-transition group"
            >
              <ArrowRightOnRectangleIcon className="h-5 w-5 mr-2 group-hover:scale-110 smooth-transition" />
              <span className="hidden sm:inline">Logout</span>
            </button>
          </div>
        </div>

        {/* Mobile menu */}
        <div className="sm:hidden">
          <div className="pt-2 pb-3 space-y-1 border-t border-navy-700/30 mt-2">
            {navigation.map((item) => {
              const Icon = item.icon;
              const isActive = isActiveRoute(item.href);
              return (
                <Link
                  key={item.name}
                  to={item.href}
                  className={`flex items-center px-3 py-2 rounded-lg text-base font-medium smooth-transition ${
                    isActive
                      ? 'bg-emerald-500/20 text-emerald-400'
                      : 'text-navy-300 hover:text-white hover:bg-navy-800/50'
                  }`}
                >
                  <Icon className={`h-5 w-5 mr-3 ${isActive ? 'text-emerald-400' : ''}`} />
                  {item.name}
                </Link>
              );
            })}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;

