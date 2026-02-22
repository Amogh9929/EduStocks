import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import { Toaster } from 'react-hot-toast';

import Login from './pages/Login';
import Signup from './pages/Signup';
import Dashboard from './pages/Dashboard';
import Lessons from './pages/Lessons';
import LessonDetail from './pages/LessonDetail';
import Trading from './pages/Trading';
import AITrainer from './pages/AITrainer';
import Profile from './pages/Profile';
import Navbar from './components/Navbar';

import './App.css';

const PrivateRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { currentUser, loading } = useAuth();

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-navy-950 via-navy-900 to-navy-800">
        <div className="text-center animate-fade-in">
          <div className="relative mb-6">
            <div className="animate-spin rounded-full h-16 w-16 border-4 border-emerald-200 border-t-emerald-500 mx-auto"></div>
            <div className="absolute inset-0 rounded-full bg-emerald-500/20 blur-lg animate-pulse"></div>
          </div>
          <div className="space-y-2">
            <h3 className="text-2xl font-bold text-white">EduStocks</h3>
            <p className="text-emerald-200 font-medium">Preparing your trading experience...</p>
          </div>
        </div>
      </div>
    );
  }

  return currentUser ? <>{children}</> : <Navigate to="/login" />;
};

const AppRoutes: React.FC = () => {
  return (
    <Router basename={process.env.PUBLIC_URL}>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route
          path="/"
          element={
            <PrivateRoute>
              <Navbar />
              <Dashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/lessons"
          element={
            <PrivateRoute>
              <Navbar />
              <Lessons />
            </PrivateRoute>
          }
        />
        <Route
          path="/lessons/:id"
          element={
            <PrivateRoute>
              <Navbar />
              <LessonDetail />
            </PrivateRoute>
          }
        />
        <Route
          path="/trading"
          element={
            <PrivateRoute>
              <Navbar />
              <Trading />
            </PrivateRoute>
          }
        />
        <Route
          path="/ai-trainer"
          element={
            <PrivateRoute>
              <Navbar />
              <AITrainer />
            </PrivateRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <PrivateRoute>
              <Navbar />
              <Profile />
            </PrivateRoute>
          }
        />
      </Routes>
    </Router>
  );
};

const App: React.FC = () => {
  return (
    <AuthProvider>
      <div className="min-h-screen bg-gray-50">
        <AppRoutes />
        <Toaster position="top-right" />
      </div>
    </AuthProvider>
  );
};

export default App;



