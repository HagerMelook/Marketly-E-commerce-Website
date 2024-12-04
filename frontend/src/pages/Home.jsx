import Header from "../components/Header";
import PropTypes from "prop-types";

function Home({ isAdmin }) {
  return (
    <div>
      <Header isAdmin={isAdmin} />
      <h1>Welcome to the Home Page!</h1>
    </div>
  );
}

Home.propTypes = {
  isAdmin: PropTypes.bool.isRequired,
};

export default Home;
