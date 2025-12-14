import React, { useEffect, useState, useCallback } from 'react';
import { stockApi, portfolioApi } from '../services/api';
import { Stock, Portfolio } from '../services/api';
import { ArrowTrendingUpIcon, ArrowTrendingDownIcon } from '@heroicons/react/24/outline';
import toast from 'react-hot-toast';

const Trading: React.FC = () => {
  const [stocks, setStocks] = useState<Stock[]>([]);
  const [portfolio, setPortfolio] = useState<Portfolio | null>(null);
  const [selectedStock, setSelectedStock] = useState<Stock | null>(null);
  const [quantity, setQuantity] = useState('');
  const [action, setAction] = useState<'buy' | 'sell'>('buy');
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  const loadData = useCallback(async () => {
    try {
      const [stocksData, portfolioData] = await Promise.all([
        stockApi.getStocks(),
        portfolioApi.getPortfolio().catch(() => null),
      ]);
      setStocks(stocksData);
      setPortfolio(portfolioData);
      if (selectedStock) {
        const updated = stocksData.find(s => s.symbol === selectedStock.symbol);
        if (updated) setSelectedStock(updated);
      }
    } catch (error) {
      toast.error('Failed to load stock data');
    } finally {
      setLoading(false);
    }
  }, [selectedStock]);

  useEffect(() => {
    loadData();
    // Refresh data every 30 seconds for real-time updates
    const interval = setInterval(loadData, 30000);
    return () => clearInterval(interval);
  }, [loadData]);

  const handleTrade = async () => {
    if (!selectedStock || !quantity || parseInt(quantity) <= 0) {
      toast.error('Please enter a valid quantity');
      return;
    }

    try {
      if (action === 'buy') {
        await portfolioApi.buyStock(selectedStock.symbol, parseInt(quantity));
        toast.success(`Successfully bought ${quantity} shares of ${selectedStock.symbol}`);
      } else {
        await portfolioApi.sellStock(selectedStock.symbol, parseInt(quantity));
        toast.success(`Successfully sold ${quantity} shares of ${selectedStock.symbol}`);
      }
      setQuantity('');
      loadData();
    } catch (error: unknown) {
      const err = error as { response?: { data?: { message?: string } } };
      toast.error(err.response?.data?.message || 'Trade failed');
    }
  };

  const filteredStocks = stocks.filter(stock =>
    stock.symbol.toLowerCase().includes(searchTerm.toLowerCase()) ||
    stock.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getHolding = (symbol: string) => {
    return portfolio?.holdings.find(h => h.symbol === symbol);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-2">Trading Floor</h1>
          <p className="text-gray-600">Trade with dummy currency using real market data</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Stock List */}
          <div className="lg:col-span-2">
            <div className="bg-white rounded-xl shadow-lg p-6">
              <div className="mb-4">
                <input
                  type="text"
                  placeholder="Search stocks..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                />
              </div>

              <div className="space-y-2 max-h-96 overflow-y-auto">
                {loading ? (
                  <div className="flex items-center justify-center py-12">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
                  </div>
                ) : (
                  filteredStocks.map((stock) => {
                    const holding = getHolding(stock.symbol);
                    return (
                      <div
                        key={stock.symbol}
                        onClick={() => setSelectedStock(stock)}
                        className={`p-4 rounded-lg cursor-pointer transition-colors ${
                          selectedStock?.symbol === stock.symbol
                            ? 'bg-primary-50 border-2 border-primary-600'
                            : 'bg-gray-50 hover:bg-gray-100 border-2 border-transparent'
                        }`}
                      >
                        <div className="flex items-center justify-between">
                          <div>
                            <p className="font-bold text-gray-900">{stock.symbol}</p>
                            <p className="text-sm text-gray-600">{stock.name}</p>
                            {holding && (
                              <p className="text-xs text-primary-600 mt-1">
                                You own: {holding.quantity} shares
                              </p>
                            )}
                          </div>
                          <div className="text-right">
                            <p className="font-bold text-gray-900">${stock.price.toFixed(2)}</p>
                            <div className={`flex items-center justify-end ${stock.change >= 0 ? 'text-success-600' : 'text-red-600'}`}>
                              {stock.change >= 0 ? (
                                <ArrowTrendingUpIcon className="h-4 w-4 mr-1" />
                              ) : (
                                <ArrowTrendingDownIcon className="h-4 w-4 mr-1" />
                              )}
                              <span className="text-sm">{stock.changePercent.toFixed(2)}%</span>
                            </div>
                          </div>
                        </div>
                      </div>
                    );
                  })
                )}
              </div>
            </div>
          </div>

          {/* Trading Panel */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-xl shadow-lg p-6 sticky top-4">
              <h2 className="text-2xl font-bold text-gray-900 mb-4">Trade</h2>

              {selectedStock ? (
                <>
                  <div className="mb-4 p-4 bg-gray-50 rounded-lg">
                    <p className="font-bold text-gray-900">{selectedStock.symbol}</p>
                    <p className="text-sm text-gray-600">{selectedStock.name}</p>
                    <p className="text-2xl font-bold text-gray-900 mt-2">
                      ${selectedStock.price.toFixed(2)}
                    </p>
                    {getHolding(selectedStock.symbol) && (
                      <p className="text-sm text-gray-600 mt-2">
                        Your holdings: {getHolding(selectedStock.symbol)?.quantity} shares
                      </p>
                    )}
                  </div>

                  <div className="mb-4">
                    <div className="flex space-x-2 mb-4">
                      <button
                        onClick={() => setAction('buy')}
                        className={`flex-1 py-2 px-4 rounded-lg font-medium ${
                          action === 'buy'
                            ? 'bg-success-600 text-white'
                            : 'bg-gray-200 text-gray-700'
                        }`}
                      >
                        Buy
                      </button>
                      <button
                        onClick={() => setAction('sell')}
                        className={`flex-1 py-2 px-4 rounded-lg font-medium ${
                          action === 'sell'
                            ? 'bg-red-600 text-white'
                            : 'bg-gray-200 text-gray-700'
                        }`}
                      >
                        Sell
                      </button>
                    </div>

                    <input
                      type="number"
                      placeholder="Quantity"
                      value={quantity}
                      onChange={(e) => setQuantity(e.target.value)}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg mb-2 focus:ring-2 focus:ring-primary-500"
                    />

                    {quantity && selectedStock && (
                      <p className="text-sm text-gray-600 mb-4">
                        Total: ${(parseFloat(quantity) * selectedStock.price).toFixed(2)}
                      </p>
                    )}

                    <button
                      onClick={handleTrade}
                      className={`w-full py-3 px-4 rounded-lg font-medium text-white ${
                        action === 'buy'
                          ? 'bg-gradient-to-r from-success-600 to-success-700 hover:from-success-700 hover:to-success-800'
                          : 'bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800'
                      }`}
                    >
                      {action === 'buy' ? 'Buy' : 'Sell'} Stock
                    </button>
                  </div>
                </>
              ) : (
                <p className="text-gray-500 text-center py-8">
                  Select a stock to start trading
                </p>
              )}

              {/* Portfolio Summary */}
              <div className="mt-6 pt-6 border-t border-gray-200">
                <h3 className="font-bold text-gray-900 mb-2">Portfolio</h3>
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <span className="text-gray-600">Balance</span>
                    <span className="font-bold text-gray-900">
                      ${portfolio?.balance.toFixed(2) || '0.00'}
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Total Value</span>
                    <span className="font-bold text-gray-900">
                      ${portfolio?.totalValue.toFixed(2) || '0.00'}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Trading;



