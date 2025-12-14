import axios from 'axios';
import { getStocks, getPortfolio, buyStock, sellStock } from './api';

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('API Service', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getStocks', () => {
    test('fetches stocks successfully', async () => {
      const mockStocks = [
        { symbol: 'AAPL', name: 'Apple Inc.', price: 150.00 },
        { symbol: 'GOOGL', name: 'Alphabet Inc.', price: 2800.00 }
      ];

      mockedAxios.get.mockResolvedValueOnce({ data: mockStocks });

      const result = await getStocks();
      
      expect(mockedAxios.get).toHaveBeenCalledWith('/api/stocks');
      expect(result).toEqual(mockStocks);
    });
  });

  describe('getPortfolio', () => {
    test('fetches user portfolio', async () => {
      const mockPortfolio = {
        userId: 'test-123',
        cash: 10000,
        holdings: []
      };

      mockedAxios.get.mockResolvedValueOnce({ data: mockPortfolio });

      const result = await getPortfolio();
      
      expect(mockedAxios.get).toHaveBeenCalledWith('/api/portfolio');
      expect(result).toEqual(mockPortfolio);
    });
  });

  describe('buyStock', () => {
    test('executes buy order successfully', async () => {
      const buyRequest = {
        symbol: 'AAPL',
        quantity: 10,
        price: 150.00
      };

      mockedAxios.post.mockResolvedValueOnce({ data: { success: true } });

      await buyStock(buyRequest);
      
      expect(mockedAxios.post).toHaveBeenCalledWith('/api/portfolio/buy', buyRequest);
    });
  });

  describe('sellStock', () => {
    test('executes sell order successfully', async () => {
      const sellRequest = {
        symbol: 'AAPL',
        quantity: 5,
        price: 160.00
      };

      mockedAxios.post.mockResolvedValueOnce({ data: { success: true } });

      await sellStock(sellRequest);
      
      expect(mockedAxios.post).toHaveBeenCalledWith('/api/portfolio/sell', sellRequest);
    });
  });
});
