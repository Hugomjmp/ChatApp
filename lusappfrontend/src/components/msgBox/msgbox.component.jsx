import React, {useEffect, useRef} from 'react';
import {useSound} from "react-sounds";


function MsgBox(props){
    const {messages,userData} = props;
    const msgBoxRef = useRef(null);
    const isInitialLoad = useRef(true);
    const { play } = useSound('notification/notification');
    console.log(messages);
    useEffect(() => {
        if(!messages || messages.length === 0) return;

        if (msgBoxRef.current) {
            msgBoxRef.current.scrollTop = msgBoxRef.current.scrollHeight;
        }
/*        if (messages && messages.length > 0) {
            const lastMessage = messages[messages.length - 1];
            if (lastMessage.senderID !== userData.id) {
                play();
            }
        }*/
        if (isInitialLoad.current) {
            isInitialLoad.current = false;
            return;
        }
        const lastMessage = messages[messages.length - 1];
        if (lastMessage.senderID !== userData.id) {
            play();
        }
    },[messages]);
    return (
        <div className="msgBox" ref={msgBoxRef}>
            {messages && messages.map((msg, index) => (
                <div
                    key={index}
                    className={msg.senderID === userData.id ? "message-sent" : "message-received"}>
                    {msg.content}
                </div>
            ))}
        </div>
    );
}


export default MsgBox;