import React, {useState} from 'react';
import {STRINGS} from "../../constants/strings";


function TypeBox(props){
    const {onSendMessage, userData} = props;
    const [message, setMessage] = useState("");
    const handleSubmit = (event) => {
        event.preventDefault();

        if (message.trim() !== ""){
            if(onSendMessage){
                onSendMessage(message);
            }
            setMessage("");
        }

    }

    return (

        <div className="type-box">
            <div className="media-container">
                <button className="emoji-container">
                <span className="emoji-icon">
                    <i className="bi bi-emoji-smile"></i>
                </span>
                </button>
                <button className="file-container">
                <span className="file-icon">
                    <i className="bi bi-paperclip"></i>
                </span>
                </button>
            </div>
            {userData && (
                <form
                    className="message-container"
                    onSubmit={handleSubmit}
                >
                    <input
                        className="message-input"
                        type="text"
                        placeholder={STRINGS[userData.language].WriteMessage}
                        value={message}
                        onChange={(event) => setMessage(event.target.value)}
                    ></input>
                </form>
            )}
            <div className="send-container">
                <button
                    className="send-button"
                    onClick={handleSubmit}
                >
                    <span className="send-icon">
                        <i className="bi bi-send"></i>
                    </span>
                </button>
            </div>
        </div>

    );
}

export default TypeBox;