import { useState } from "react";
import PropTypes from "prop-types";
import noImage from "../../../../assets/no-image.jpg";
import "./ImagesContainer.css";

function ImageContainer({ images }) {
  const [currentIndex, setCurrentIndex] = useState(0);

  const goToPreviousImage = () => {
    setCurrentIndex((prevIndex) => (prevIndex === 0 ? images.length - 1 : prevIndex - 1));
  };

  const goToNextImage = () => {
    setCurrentIndex((prevIndex) => (prevIndex === images.length - 1 ? 0 : prevIndex + 1));
  };

  return (
    <div className="image-container">
      {images && images.length > 0 ? (
        <>
          <img
            className="image"
            src={images[currentIndex]}
            alt={`Image ${currentIndex + 1}`}
          />

          {images.length > 1 && (<>
            <button
              className="image-button image-button-left"
              onClick={goToPreviousImage}
            >
              &lt;
            </button>

            <button
              className="image-button image-button-right"
              onClick={goToNextImage}
            >
              &gt;
            </button>
          </>)}
        </>
      ) : (
        <p className="no-image-placeholder">
          <img src={noImage} alt="no image" />
        </p>
      )}
    </div>
  );
}

ImageContainer.propTypes = {
  images: PropTypes.arrayOf(PropTypes.string),
};

export default ImageContainer;
