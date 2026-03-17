import React from 'react';


function Header(props) {
    const { chatSelected, contactPictures } = props;

    return (
            <div className="navbar">
                {chatSelected && (
                <div className="navbar-contact-container">
                    <button className="navbar-contacts-item">
                    <span className="current-person-item">
                        <img
                            className="current-person-picture"
                            src={contactPictures[chatSelected.id]}
                        ></img>
                        <label className="current-person-name">
                            {chatSelected.profileName}
                        </label>
                    </span>
                    </button>
                </div>
                )}
                <div className="voip-container">
                    <button className="phone">
                        <i className="bi bi-telephone"></i>
                    </button>
                    <div className="voip-divider"></div>
                    <button className="camera-video">
                        <i className="bi bi-camera-video"> </i>
                    </button>
                </div>

            </div>

    );
}

export default Header;