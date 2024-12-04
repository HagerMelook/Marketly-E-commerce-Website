import "./Header.css";
import logo from "../assets/logo-2.2.png";
import cartIcon from "../assets/cart-icon.svg";
import profileIcon from "../assets/profile-icon.svg";
import searchIcon from "../assets/search-icon.svg";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";

function Header({ isAdmin }) {
  return (
    <header className="header">
      <div className="logo">
        <Link to="/">
          <img src={logo} alt="Company Logo" />
        </Link>
      </div>

      <form className="search-bar">
        <input type="text" name="search" placeholder="Search for products..." />
        <button type="submit">
          <img src={searchIcon} className="icons" />
        </button>
      </form>

      <div className="icons">
        <span className="orders-text">Orders</span>
        {isAdmin && (
          <Link to="/admin-dashboard" className="orders-text">
            Admin Dashboard
          </Link>
        )}
        <img src={cartIcon} alt="Cart" title="View Cart" />
        <img src={profileIcon} alt="Profile" title="Your Profile" />
      </div>
    </header>
  );
}

Header.propTypes = {
  isAdmin: PropTypes.bool,
};

export default Header;
