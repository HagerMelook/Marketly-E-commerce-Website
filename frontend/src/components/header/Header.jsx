import "./Header.css";
import logo from "../../assets/logo-2.2.png";
import cartIcon from "../../assets/cart-icon.svg";
import profileIcon from "../../assets/profile-icon.svg";
import searchIcon from "../../assets/search-icon.svg";
import SmallProductCard from "../small product card/SmallProductCard";
import PropTypes from "prop-types";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
// import { use } from "react";

function Header({ isAdmin, isVendor }) {
  const [searchWord, setSearchWord] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [searchTableState, setSearchTableState] = useState(false);
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchWord.trim()) {
      navigate(`/search?word=${searchWord}`);
    }
  };

  //   useEffect(() => {
  //     setSearchTableState(false);
  //   }, [searchResults]);

  const fetchInstantSearchResults = (searchWord) => {
    return fetch(`http://localhost:8080/Search/${searchWord}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setSearchResults(data);
        return data;
      })
      .catch((error) => {
        console.error(error);
        throw error;
      });
  };

  return (
    <header className="header">
      <div className="logo">
        <Link to="/home">
          <img src={logo} alt="Company Logo" />
        </Link>
      </div>

      <div className="searchBox">
        <form
          className="search-bar"
          onSubmit={(e) => {
            setSearchTableState(false);
            handleSearch(e);
          }}
          onBlur={() => setSearchTableState(false)}
        >
          <input
            type="text"
            name="search"
            placeholder="Search for products..."
            value={searchWord}
            onChange={(e) => {
              setSearchWord(e.target.value);
              fetchInstantSearchResults(e.target.value);
              setSearchTableState(true);
            }}
          />
          <button type="submit">
            <img src={searchIcon} className="icons" />
            {/* <Link to="/search-page"></Link> */}
          </button>
        </form>
        {searchTableState && (
          <>
            {searchResults.length === 0 ? (
              <table className="searchTable">
                <p>No Products Found..</p>
              </table>
            ) : (
              <table className="searchTable">
                {searchResults.slice(0, 6).map((result) => (
                  <SmallProductCard key={result.id} product={result} />
                ))}
              </table>
            )}
          </>
        )}
      </div>

      <div className="icons">
        <Link to="/orders" className="orders-text">
          Orders
        </Link>
        {isAdmin && (
          <Link to="/admin-dashboard" className="orders-text">
            Admin Dashboard
          </Link>
        )}

        {isVendor && (
          <Link to="/inventory" className="orders-text">
            Inventory
          </Link>
        )}
        <Link to="/cart">
          <img src={cartIcon} alt="Cart" title="View Cart" />
        </Link>

        {isVendor && (
          <Link to="/VendorOrders" className="orders-text">
            Orders History
          </Link>
        )}

        <Link to="/editProfile">
          <img src={profileIcon} alt="Profile" title="Your Profile" />
        </Link>
      </div>
    </header>
  );
}

Header.propTypes = {
  isAdmin: PropTypes.bool,
  isVendor: PropTypes.bool,
};

export default Header;
