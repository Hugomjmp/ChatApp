import React, {useState} from 'react';
import {STRINGS} from "../../constants/strings";


function Side(props){
    const { userData,
        newChat,
        pendingContactList,
        chatList,
        contactPictures,
        currentChatID,
        sideContactsEnabled
    } = props;

    return (
        <div
            className={`sidebarContacts ${sideContactsEnabled ? "open" : ""}` }
        >
            {userData && (
                <h3 className="sidebarContacts-title">
                    {STRINGS[userData.language].Chats}
                    {pendingContactList && pendingContactList.length > 0 &&(
                    <div className="sidebarContacts-pending-number">
                        <p
                            className="pending-number"
                        >
                            {pendingContactList.length}
                        </p>
                    </div>
                    )}
                    <button
                        className="sidebarContacts-new-chat-btn"
                        onClick={newChat}
                    >
                        <i className="bi bi-pencil-square"></i>
                    </button>
                </h3>
            )}
            {userData && (
                <form className="sidebarContacts-form">
                    <input
                        className="search-bar"
                        type="text"
                        placeholder={STRINGS[userData.language].Search}
                    ></input>
                </form>
                )}
            <div className="sidebarContacts-container">
                {chatList && chatList.map((chat, index) => {
                    const otherUser = chat.chatParticipantsList.find(u => u.id !== userData.id);
                    return (
                        <div key={index} className="sidebarContacts-item-container">
                            <button
                                key={index}
                                className="sidebarContacts-item"
                                onClick={() => {
                                    currentChatID(chat.ID,otherUser.id);
                                }}
                            >
                                <img
                                    className="sidebarContacts-picture"
                                    src={contactPictures[otherUser.id]}
                                ></img>
                                <div className="sidebarContacts-item-description">
                                    <span className="person-icon">
                                        {otherUser.name}
                                    </span>
                                    <span className="lastMessage">
                                        {chat.lastMessage
                                            ? (chat.lastMessage.length > 17
                                                ? chat.lastMessage.slice(0, 20) + "..."
                                                : chat.lastMessage)
                                            : ""}
                                    </span>
                                </div>
                            </button>
                        </div>
                    );
                })}
            </div>
        </div>
    )

}

export default Side;