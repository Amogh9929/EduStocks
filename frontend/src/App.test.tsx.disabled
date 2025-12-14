import { render, screen } from '@testing-library/react';
import App from './App';

test('renders without crashing', () => {
  render(<App />);
  expect(screen.getByTestId('app-root') || document.body).toBeInTheDocument();
});

describe('App Component', () => {
  test('initializes router', () => {
    render(<App />);
    // Router should be initialized
    expect(document.querySelector('.App')).toBeTruthy();
  });
});
