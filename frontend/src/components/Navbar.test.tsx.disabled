import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Navbar from './Navbar';
import { AuthProvider } from '../contexts/AuthContext';

const MockedNavbar = () => (
  <BrowserRouter>
    <AuthProvider>
      <Navbar />
    </AuthProvider>
  </BrowserRouter>
);

describe('Navbar Component', () => {
  test('renders app title', () => {
    render(<MockedNavbar />);
    const titleElement = screen.getByText(/EduStocks/i);
    expect(titleElement).toBeInTheDocument();
  });

  test('renders navigation links when authenticated', () => {
    render(<MockedNavbar />);
    // Add more specific tests based on authentication state
  });
});
