import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { updateState } from "../../ReduxManager/dataStoreSlice";

function App() {
  // Access the imageFile state from the Redux store
  const imageFile = useSelector((state) => state.dataStore.imageFile);

  // Access the dispatch function from Redux
  const dispatch = useDispatch();

  // Handle file input change
  function handleChange(e) {
    // This function is used to update 'imageFile' in dataStoreSlice with the user's input,
    // which will be reflected in personalInfo as the profile image.

    const file = e.target.files[0];
    const fileType = file["type"];
    const validImageTypes = ["image/gif", "image/jpeg", "image/png"];

    if (validImageTypes.includes(fileType)) {
      const temp = URL.createObjectURL(file);

      // Dispatch the updateState action to update the imageFile in the Redux store
      dispatch(
        updateState({
          key: "imageFile",
          value: temp,
        })
      );
    } else {
      alert("Uploaded file type should be jpg/png!");
    }
  }

  return (
    <div className="container">
      <div className="row">
        {/* Display the profile image */}
        <img
          style={{
            height: "150px",
            width: "150px", // Increased width for a more balanced look
            backgroundColor: "lightgray", // Improved readability
            borderRadius: "50%", // Make the image round
            objectFit: "cover", // Maintain aspect ratio
          }}
          src={imageFile}
          alt="Profile"
        />
      </div>
      <div className="row">
        {/* File input for changing the profile image */}
        <input
          type="file"
          onChange={handleChange}
          accept=".gif, .jpeg, .jpg, .png"
        />
      </div>
    </div>
  );
}

export default App;
