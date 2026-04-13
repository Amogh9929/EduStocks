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
    <div className="min-h-screen bg-gradient-to-br from-navy-950 via-navy-900 to-navy-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-white mb-2">Trading Floor</h1>
          <p className="text-emerald-200">Trade with dummy currency using real market data</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          {/* Stock Grid */}
          <div className="lg:col-span-3">
            {/* Search Bar */}
            <div className="mb-6">
              <input
                type="text"
                placeholder="Search stocks by symbol or name..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full px-4 py-3 border border-navy-600 placeholder-navy-400 text-white bg-navy-800/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 smooth-transition backdrop-blur-sm"
              />
            </div>

            {/* Stock Grid Display */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
              {loading ? (
                <div className="col-span-full flex items-center justify-center py-12">
                  <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-emerald-500 mx-auto mb-4"></div>
                    <p className="text-emerald-200">Loading stocks...</p>
                  </div>
                </div>
              ) : filteredStocks.length > 0 ? (
                filteredStocks.map((stock) => {
                  const holding = getHolding(stock.symbol);
                  const isSelected = selectedStock?.symbol === stock.symbol;
                  return (
                    <div
                      key={stock.symbol}
                      onClick={() => setSelectedStock(stock)}
                      className={`p-4 rounded-xl cursor-pointer transition-all transform hover:scale-105 ${
                        isSelected
                          ? 'glass-card ring-2 ring-emerald-500 border border-emerald-400'
                          : 'glass-card hover:ring-1 hover:ring-emerald-400 border border-navy-600'
                      }`}
                    >
                      <div className="flex items-start justify-between mb-3">
                        <div>
                          <p className="font-bold text-white text-lg">{stock.symbol}</p>
                          <p className="text-sm text-emerald-200">{stock.name}</p>
                        </div>
                        <div className={`flex items-center px-2 py-1 rounded-lg ${stock.change >= 0 ? 'bg-emerald-500/20' : 'bg-red-500/20'}`}>
                          {stock.change >= 0 ? (
                            <ArrowTrendingUpIcon className="h-4 w-4 mr-1 text-emerald-400" />
                          ) : (
                            <ArrowTrendingDownIcon className="h-4 w-4 mr-1 text-red-400" />
                          )}
                          <span className={`text-sm font-semibold ${stock.change >= 0 ? 'text-emerald-400' : 'text-red-400'}`}>
                            {stock.changePercent.toFixed(2)}%
                          </span>
                        </div>
                      </div>
                      <div className="mb-3">
                        <p className="text-2xl font-bold text-white">${stock.price.toFixed(2)}</p>
                        <p className="text-xs text-navy-300">
                          {stock.change >= 0 ? '+' : ''}{stock.change.toFixed(2)}
                        </p>
                      </div>
                      <div className="pt-3 border-t border-navy-600">
                        <p className="text-xs text-navy-300">Vol: {(stock.volume / 1000000).toFixed(1)}M</p>
                        {holding && (
                          <p className="text-xs text-emerald-300 mt-1 font-medium">
                            Holdings: {holding.quantity} shares
                          </p>
                        )}
                      </div>
                    </div>
                  );
                })
              ) : (
                <div className="col-span-full text-center py-12">
                  <p className="text-navy-300">No stocks found matching "{searchTerm}"</p>
                </div>
              )}
            </div>
          </div>

          {/* Trading Panel */}
          <div className="lg:col-span-1">
            <div className="glass-card rounded-xl p-6 sticky top-4 border border-navy-600">
              <h2 className="text-2xl font-bold text-white mb-4">Quick Trade</h2>

              {selectedStock ? (
                <>
                  <div className="mb-4 p-4 bg-navy-800/50 rounded-lg border border-navy-600">
                    <p className="font-bold text-white text-lg">{selectedStock.symbol}</p>
                    <p className="text-sm text-emerald-200">{selectedStock.name}</p>
                    <p className="text-3xl font-bold text-emerald-400 mt-2">
                      ${selectedStock.price.toFixed(2)}
                    </p>
                    {getHolding(selectedStock.symbol) && (
                      <p className="text-sm text-emerald-300 mt-3 font-medium">
                        Your holdings: {getHolding(selectedStock.symbol)?.quantity} shares
                      </p>
                    )}
                  </div>

                  <div className="mb-4">
                    <label className="block text-sm font-medium text-emerald-200 mb-3">Action</label>
                    <div className="flex space-x-2 mb-4">
                      <button
                        onClick={() => setAction('buy')}
                        className={`flex-1 py-2 px-4 rounded-lg font-medium transition-all smooth-transition ${
                          action === 'buy'
                            ? 'bg-emerald-600 text-white shadow-lg'
                            : 'bg-navy-700 text-emerald-200 hover:bg-navy-600'
                        }`}
                      >
                        Buy
                      </button>
                      <button
                        onClick={() => setAction('sell')}
                        className={`flex-1 py-2 px-4 rounded-lg font-medium transition-all smooth-transition ${
                          action === 'sell'
                            ? 'bg-red-600 text-white shadow-lg'
                            : 'bg-navy-700 text-red-200 hover:bg-navy-600'
                        }`}
                      >
                        Sell
                      </button>
                    </div>

                    <div className="mb-3">
                      <label className="block text-sm font-medium text-emerald-200 mb-2">Quantity</label>
                      <input
                        type="number"
                        placeholder="Enter quantity"
                        value={quantity}
                        onChange={(e) => setQuantity(e.target.value)}
                        className="w-full px-4 py-2 border border-navy-600 placeholder-navy-400 text-white bg-navy-800/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 smooth-transition"
                      />
                    </div>

                    {quantity && selectedStock && (
                      <div className="mb-4 p-3 bg-navy-800/50 rounded-lg border border-navy-600">
                        <div className="flex justify-between items-center">
                          <span className="text-sm text-navy-300">Total Cost</span>
                          <span className="font-bold text-emerald-400">
                            ${(parseFloat(quantity) * selectedStock.price).toFixed(2)}
                          </span>
                        </div>
                      </div>
                    )}

                    <button
                      onClick={handleTrade}
                      className={`w-full py-3 px-4 rounded-lg font-bold text-white smooth-transition transform hover:scale-105 ${
                        action === 'buy'
                          ? 'bg-gradient-to-r from-emerald-600 to-emerald-700 hover:from-emerald-700 hover:to-emerald-800 shadow-lg'
                          : 'bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 shadow-lg'
                      }`}
                    >
                      {action === 'buy' ? '🔼 Buy' : '🔽 Sell'} Stock
                    </button>
                  </div>
                </>
              ) : (
                <p className="text-emerald-300 text-center py-8">
                  Select a stock from the grid to get started
                </p>
              )}

              {/* Portfolio Summary */}
              <div className="mt-6 pt-6 border-t border-navy-600">
                <h3 className="font-bold text-white mb-4">Portfolio Summary</h3>
                <div className="space-y-3">
                  <div className="flex justify-between items-center p-3 bg-navy-800/50 rounded-lg">
                    <span className="text-sm text-navy-300">Available Balance</span>
                    <span className="font-bold text-emerald-400">
                      ${portfolio?.balance.toFixed(2) || '0.00'}
                    </span>
                  </div>
                  <div className="flex justify-between items-center p-3 bg-navy-800/50 rounded-lg">
                    <span className="text-sm text-navy-300">Portfolio Value</span>
                    <span className="font-bold text-white">
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



