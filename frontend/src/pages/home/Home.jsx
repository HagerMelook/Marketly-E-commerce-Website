import Header from "../../components/header/Header";
import PropTypes from "prop-types";

function Home({ isAdmin , isVendor }) {
  return (
    <div>
      <Header isAdmin={isAdmin} isVendor = {isVendor}/>
      <h1>Welcome to the Home Page!</h1>
    </div>
  );
}

Home.propTypes = {
  isAdmin: PropTypes.bool.isRequired,
  isVendor: PropTypes.bool.isRequired
};

export default Home;
