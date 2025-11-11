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
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return currentUser ? <>{children}</> : <Navigate to="/login" />;
};

const AppRoutes: React.FC = () => {
  return (
    <Router>
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



