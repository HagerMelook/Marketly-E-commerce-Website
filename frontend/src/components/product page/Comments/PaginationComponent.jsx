import PropTypes from "prop-types";
import "./Comments.css";

const PaginationComponent = ({ onPrevious, onNext, currentPage, hasNext }) => {
  return (
    <div className="paginationContainer">
      <button className="pagination-button" onClick={onPrevious} disabled={currentPage === 1}>
        Previous
      </button>
      <span className="pageIndicator">Page {currentPage}</span>
      <button className="pagination-button" onClick={onNext} disabled={!hasNext}>
        Next
      </button>
    </div>
  );
};

PaginationComponent.propTypes = {
  onPrevious: PropTypes.func.isRequired,
  onNext: PropTypes.func.isRequired,
  currentPage: PropTypes.number.isRequired,
  hasNext: PropTypes.bool.isRequired,
};

export default PaginationComponent;