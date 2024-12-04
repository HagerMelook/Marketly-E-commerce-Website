import PropTypes from "prop-types";
import "./Sidebar.css";

function Sidebar({ handleOnClick, activeIndex }) {
  const options = [
    { id: 1, label: "Account Management" },
    { id: 2, label: "User Feedback" },
    { id: 3, label: "Vendor Requests" },
  ];

  return (
    <div className="sidebar">
      {options.map((option) => (
        <button
          key={option.id}
          className={activeIndex === option.id ? "active" : ""}
          onClick={() => handleOnClick(option.id)}
        >
          {option.label}
        </button>
      ))}
    </div>
  );
}

Sidebar.propTypes = {
  handleOnClick: PropTypes.func.isRequired,
  activeIndex: PropTypes.number.isRequired,
};

export default Sidebar;
