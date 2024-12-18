import { useState } from "react";
import PropTypes from "prop-types";
import noImage from "../../../../assets/no-image.jpg"
import "./ImagesContainer.css"

function ImageContainer ({ images }) {

  const [currentIndex, setCurrentIndex] = useState(0);

  const goToPreviousImage = () => {
    setCurrentIndex((prevIndex) => (prevIndex === 0 ? images.length - 1 : prevIndex - 1));
  };

  const goToNextImage = () => {
    setCurrentIndex((prevIndex) => (prevIndex === images.length - 1 ? 0 : prevIndex + 1));
  };

  return (
    <div className="image-container" style={{ position: "relative", width: "100%", maxWidth: "500px", height: "500px", alignContent: "center", backgroundColor: "#f9f9f9", 
        padding: "20px", borderRadius: "8px", borderStyle: "groove", borderWidth: "1px"}}>
      {images && images.length > 0 ? (
        <>
          <img
            src={images[currentIndex]}
            alt={`Image ${currentIndex + 1}`}
            style={{ width: "100", height: "100", display: "block", alignSelf: "center", justifySelf: "center", maxWidth: "500px", maxHeight: "500px" }}
          />
          
          <button
            className="image-button"
            onClick={goToPreviousImage}
            style={{
              position: "absolute",
              top: "50%",
              left: "10px",
              transform: "translateY(-50%)",
              backgroundColor: "rgba(0, 0, 0, 0.5)",
              color: "white",
              border: "none",
              padding: "10px",
              cursor: "pointer"
            }}
          >
            &lt;
          </button>

          <button
            className="image-button"
            onClick={goToNextImage}
            style={{
              position: "absolute",
              top: "50%",
              right: "10px",
              transform: "translateY(-50%)",
              backgroundColor: "rgba(0, 0, 0, 0.5)",
              color: "white",
              border: "none",
              padding: "10px",
              cursor: "pointer",
            }}
          >
            &gt;
          </button>
        </>
      ) : (
        <p>
          <img
          src={noImage}
          alt="no image"
          style={{ width: "100", height: "100", display: "block", alignSelf: "center", justifySelf: "center", maxWidth: "500px", maxHeight: "500px" }}
          />
        </p>
      )}
    </div>
  );
};

ImageContainer.propTypes = {
    images: PropTypes.arrayOf(PropTypes.string)
};

export default ImageContainer;
