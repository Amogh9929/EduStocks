import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
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

  return (
    <nav className="bg-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex">
            <div className="flex-shrink-0 flex items-center">
              <Link to="/" className="text-2xl font-bold bg-gradient-to-r from-primary-600 to-success-600 bg-clip-text text-transparent">
                📚 EduStocks
              </Link>
            </div>
            <div className="hidden sm:ml-6 sm:flex sm:space-x-8">
              {navigation.map((item) => {
                const Icon = item.icon;
                return (
                  <Link
                    key={item.name}
                    to={item.href}
                    className="inline-flex items-center px-1 pt-1 border-b-2 border-transparent text-sm font-medium text-gray-500 hover:text-gray-700 hover:border-gray-300"
                  >
                    <Icon className="h-5 w-5 mr-2" />
                    {item.name}
                  </Link>
                );
              })}
            </div>
          </div>
          <div className="flex items-center">
            <span className="text-sm text-gray-700 mr-4">
              {currentUser?.email}
            </span>
            <button
              onClick={handleLogout}
              className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700"
            >
              <ArrowRightOnRectangleIcon className="h-5 w-5 mr-2" />
              Logout
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;

