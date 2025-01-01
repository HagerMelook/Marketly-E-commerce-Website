import { Link } from 'react-router-dom';
import { MessageSquare } from 'lucide-react';
import './Footer.css';

function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="footer">
      <div className="footer-container">
        <p className="footer-copyright">
          © {currentYear} Marketly. All rights reserved.
        </p>
        <nav className="footer-nav">
          <Link to ="/feedback" className="footer-link">
            <MessageSquare size={16} />
            Feedback
          </Link>
        </nav>
      </div>
    </footer>
  );
}

export default Footer;