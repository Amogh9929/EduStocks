import axios from 'axios';
import { auth } from '../firebase/config';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
});

// Add token to requests
api.interceptors.request.use(
  async (config) => {
    const user = auth.currentUser;
    if (user) {
      const token = await user.getIdToken();
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export interface Stock {
  symbol: string;
  name: string;
  price: number;
  change: number;
  changePercent: number;
  volume: number;
}

export interface Portfolio {
  userId: string;
  balance: number;
  holdings: Holding[];
  totalValue: number;
}

export interface Holding {
  symbol: string;
  quantity: number;
  averagePrice: number;
  currentPrice: number;
  totalValue: number;
  profit: number;
  profitPercent: number;
}

export interface Lesson {
  id: string;
  title: string;
  description: string;
  level: 'beginner' | 'intermediate' | 'advanced';
  content: string;
  questions: Question[];
  order: number;
}

export interface Question {
  id: string;
  question: string;
  options: string[];
  correctAnswer: number;
  explanation: string;
}

export interface UserProgress {
  userId: string;
  level: 'beginner' | 'intermediate' | 'advanced';
  completedLessons: string[];
  xp: number;
  rank: string;
}

export interface AITrainerQuestion {
  question: string;
  options: string[];
  correctAnswer: number;
  explanation: string;
  topic: string;
}

export const stockApi = {
  getStocks: async (): Promise<Stock[]> => {
    const response = await api.get('/stocks');
    return response.data;
  },
  getStock: async (symbol: string): Promise<Stock> => {
    const response = await api.get(`/stocks/${symbol}`);
    return response.data;
  },
};

export const portfolioApi = {
  getPortfolio: async (): Promise<Portfolio> => {
    const response = await api.get('/portfolio');
    return response.data;
  },
  buyStock: async (symbol: string, quantity: number): Promise<void> => {
    await api.post('/portfolio/buy', { symbol, quantity });
  },
  sellStock: async (symbol: string, quantity: number): Promise<void> => {
    await api.post('/portfolio/sell', { symbol, quantity });
  },
};

export const lessonApi = {
  getLessons: async (level?: string): Promise<Lesson[]> => {
    const params = level ? { level } : {};
    const response = await api.get('/lessons', { params });
    return response.data;
  },
  getLesson: async (id: string): Promise<Lesson> => {
    const response = await api.get(`/lessons/${id}`);
    return response.data;
  },
  completeLesson: async (id: string, score: number): Promise<void> => {
    await api.post(`/lessons/${id}/complete`, { score });
  },
};

export const progressApi = {
  getProgress: async (): Promise<UserProgress> => {
    const response = await api.get('/progress');
    return response.data;
  },
};

export const aiTrainerApi = {
  getQuestion: async (level: string, topic?: string): Promise<AITrainerQuestion> => {
    const params = topic ? { level, topic } : { level };
    const response = await api.post('/ai-trainer/question', params);
    return response.data;
  },
  submitAnswer: async (questionId: string, answer: number): Promise<{ correct: boolean; explanation: string }> => {
    const response = await api.post('/ai-trainer/answer', { questionId, answer });
    return response.data;
  },
  askQuestion: async (query: string, level: string): Promise<string> => {
    const response = await api.post('/ai-trainer/ask', { query, level });
    return response.data.response;
  },
};

export default api;



