import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import toast from 'react-hot-toast';

const Signup: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const { signup } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      toast.error('Passwords do not match', {
        style: {
          background: '#1e293b',
          color: '#f8fafc',
          border: '1px solid #ef4444',
        },
      });
      return;
    }

    if (password.length < 6) {
      toast.error('Password must be at least 6 characters', {
        style: {
          background: '#1e293b',
          color: '#f8fafc',
          border: '1px solid #ef4444',
        },
      });
      return;
    }

    try {
      setLoading(true);
      await signup(email, password);
      toast.success('Welcome to EduStocks! 🎉', {
        style: {
          background: '#022c22',
          color: '#dcfce7',
          border: '1px solid #16a34a',
        },
      });
      navigate('/');
    } catch (error: unknown) {
      const err = error as { message?: string };
      toast.error(err.message || 'Failed to create account', {
        style: {
          background: '#1e293b',
          color: '#f8fafc',
          border: '1px solid #ef4444',
        },
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-navy-950 via-navy-900 to-navy-800 py-12 px-4 sm:px-6 lg:px-8 animate-fade-in">
      {/* Background decoration */}
      <div className="absolute inset-0 overflow-hidden">
        <div className="absolute -top-40 -left-40 w-80 h-80 bg-emerald-500/20 rounded-full blur-3xl animate-pulse-slow"></div>
        <div className="absolute -bottom-40 -right-40 w-80 h-80 bg-navy-500/20 rounded-full blur-3xl animate-pulse-slow" style={{ animationDelay: '1s' }}></div>
      </div>

      <div className="relative z-10 max-w-md w-full space-y-8">
        <div className="text-center animate-fade-in-up">
          <div className="flex justify-center mb-6">
            <div className="w-20 h-20 bg-gradient-to-br from-emerald-400 to-emerald-600 rounded-full flex items-center justify-center shadow-xl">
              <span className="text-3xl">🚀</span>
            </div>
          </div>
          <h2 className="mt-6 text-center text-4xl font-bold text-white">
            Join <span className="bg-gradient-to-r from-emerald-400 to-emerald-500 bg-clip-text text-transparent">EduStocks</span>
          </h2>
          <p className="mt-2 text-center text-lg text-navy-200">
            Start your journey to financial literacy today
          </p>
        </div>

        <div className="glass-card rounded-2xl p-8 shadow-2xl animate-fade-in-up" style={{ animationDelay: '0.2s' }}>
          <form className="space-y-6" onSubmit={handleSubmit}>
            <div className="space-y-4">
              <div>
                <label htmlFor="email-address" className="block text-sm font-medium text-gray-200 mb-2">
                  Email address
                </label>
                <input
                  id="email-address"
                  name="email"
                  type="email"
                  autoComplete="email"
                  required
                  className="appearance-none relative block w-full px-4 py-3 border border-navy-600 placeholder-navy-400 text-white bg-navy-800/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 focus:z-10 text-sm smooth-transition backdrop-blur-sm"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </div>
              <div>
                <label htmlFor="password" className="block text-sm font-medium text-gray-200 mb-2">
                  Password
                </label>
                <input
                  id="password"
                  name="password"
                  type="password"
                  autoComplete="new-password"
                  required
                  className="appearance-none relative block w-full px-4 py-3 border border-navy-600 placeholder-navy-400 text-white bg-navy-800/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 focus:z-10 text-sm smooth-transition backdrop-blur-sm"
                  placeholder="Create a strong password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>
              <div>
                <label htmlFor="confirm-password" className="block text-sm font-medium text-gray-200 mb-2">
                  Confirm Password
                </label>
                <input
                  id="confirm-password"
                  name="confirm-password"
                  type="password"
                  autoComplete="new-password"
                  required
                  className="appearance-none relative block w-full px-4 py-3 border border-navy-600 placeholder-navy-400 text-white bg-navy-800/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 focus:z-10 text-sm smooth-transition backdrop-blur-sm"
                  placeholder="Confirm your password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                />
              </div>
            </div>

            <div>
              <button
                type="submit"
                disabled={loading}
                className="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-semibold rounded-lg text-white bg-gradient-to-r from-emerald-500 to-emerald-600 hover:from-emerald-600 hover:to-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500 disabled:opacity-50 smooth-transition shadow-lg hover:shadow-xl"
              >
                {loading ? (
                  <div className="flex items-center">
                    <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                    Creating your account...
                  </div>
                ) : (
                  <span className="flex items-center">
                    <span className="mr-2">🎯</span>
                    Start Learning Today
                  </span>
                )}
              </button>
            </div>

            <div className="text-center">
              <p className="text-sm text-navy-300">
                Already have an account?{' '}
                <Link 
                  to="/login" 
                  className="font-medium text-emerald-400 hover:text-emerald-300 smooth-transition"
                >
                  Sign in here →
                </Link>
              </p>
            </div>
          </form>
        </div>

        <div className="text-center animate-fade-in-up" style={{ animationDelay: '0.4s' }}>
          <p className="text-xs text-navy-400">
            🛡️ Secure • Private • Free to start
          </p>
        </div>
      </div>
    </div>
  );
};

export default Signup;

