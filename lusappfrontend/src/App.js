import {useEffect, useRef ,useState} from "react";
import {BASE_SOCKET_URL, BASE_URL} from "./constants/base_url";
import './assets/styles/App.css';
import Header from "./components/header/header.component";
import Login from "./components/login/login.component";
import SideContacts from "./components/sideContacts/sideContacts.component";
import MsgBox from "./components/msgBox/msgbox.component";
import TypeBox from "./components/typebox/typebox.component";
import User from "./components/user/user.component";
import Side from "./components/sideBar/side.component";
import Register from "./components/register/register.component";
import NewChat from "./components/newchat/newchat.component";
import NewContact from "./components/newcontact/newContact.component";

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isPreferencesEnable, setPreferencesEnable] = useState(false);
    const [userData, setUserData] = useState();
    const [isLoginMenuEnabled, setIsLoginMenuEnabled] = useState(true);
    const [isRegisterMenuEnabled, setIsRegisterMenuEnabled] = useState(false);
    const [messages, setMessages] = useState([]);
    const [chatList, setChatList] = useState([]); //<-- PARA A LISTA DOS CHATS ATIVOS
    const [recipient, setRecipient] = useState(null);
    const [newChatMenu, setNewChatMenu] = useState(false);
    const [pendingContactList, setPendigContactList] = useState([]);
    const [contactList, setContactList] = useState([]);
    const [newContact, setNewContact] = useState(false);
    const [contactPictures, setContactPictures] = useState({});
    const [chatSelected, setChatSelected] = useState(() => {
        const stored = localStorage.getItem("user");
        return stored ? JSON.parse(stored) : null;
    });
    const [sideContactsEnabled, setSideContactsEnabled] = useState(false);
    /*SOCKET*/
    const socketRef = useRef(null);
    /*-------*/
    //const [chatID, setChatID] = useState(null);
    const [chatID, setChatID] = useState(() => {
        const stored = localStorage.getItem("chatID");
        return stored ? JSON.parse(stored) : null;
    });

    const handleSideContactsMenu = () => {
        if (sideContactsEnabled) {
            setSideContactsEnabled(false);
        } else {
            setSideContactsEnabled(true);
        }
    }
    const handleNewContact = () => {
        if (newContact) {
            setNewContact(false);
        } else {
            setNewContact(true);
        }
    }
    const handleNewChat = () => {
        if (newChatMenu) {
            setNewChatMenu(false);
        } else {
            setNewChatMenu(true);
        }
    }
    const handleLoginSuccess = (data) => {
      setUserData(data);
      setIsLoggedIn(true);
    };
    const handleRegisterMenu = () => {
        if (isRegisterMenuEnabled){
            setIsRegisterMenuEnabled(false);
            setIsLoginMenuEnabled(true);
        } else {
            setIsRegisterMenuEnabled(true);
            setIsLoginMenuEnabled(false);
        }
    }
    const handleLogout = () => {
        fetch(`${BASE_URL}/logout`, {method: 'POST'})
        localStorage.removeItem("token");
        setIsLoggedIn(false);
    };
    const handlePreferences = () => {
      if (isPreferencesEnable) {
          setPreferencesEnable(false);
      } else {
          setPreferencesEnable(true);
      }
    }
    const handleCurrentChatID = (ID, otherUserID) => {
        setChatID(ID);
        setRecipient(otherUserID);
        handleReceiveMessages(ID)
    }
    useEffect(() => {
        if(!isLoggedIn) return;
        if (!chatID) return;

        if(chatID){
            localStorage.setItem("chatID", JSON.stringify(chatID));
            handleReceiveMessages(chatID);
        }

    }, [chatID,isLoggedIn]);
    const handleReceiveMessages = (chatID) => {
        const token = localStorage.getItem("token");
        fetch(`${BASE_URL}/messages?chatID=${chatID}&userID=${userData?.id}`, {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${token}`
            }
        }).then(res => {
            if (res.ok) return res.json();
        }).then(data => {
            //setMessages(data);
            setMessages(data);
        }).catch(err => {
            console.log(err);
        });
    }

    const handleMessage = (messageToSend) => {
        //setMessage(messageToSend); // guarda localmente a última mensagem

        if (socketRef.current && socketRef.current.readyState === WebSocket.OPEN) {
            const payload = {
                chatID: chatID,
                senderID: userData?.id,
                recipientID: recipient,
                content: messageToSend,
                /*timestamp: new Date().toISOString()*/
            };

            socketRef.current.send(JSON.stringify(payload));
            //setMessages(prev => [...prev, payload]);
            console.log("Mensagem enviada pelo socket:", payload);
        } else {
            console.warn("Socket não está pronto para enviar");
        }
    };
    useEffect(() => {
        if (!socketRef.current) return;

        socketRef.current.onmessage = (event) => {
            try {
                const msg = JSON.parse(event.data);
                console.log("Mensagem recebida:", msg);
                setMessages(prev => [...prev, msg]);
            } catch (error) {
                console.error("Erro ao parsear mensagem:", error);
            }
        };
        return () => {
            socketRef.current.onmessage = null;
        };
    }, [isLoggedIn]);
    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            fetch(`${BASE_URL}/profile`, {
                headers: { Authorization: `Bearer ${token}` }
            })
                .then(res => {
                    if (!res.ok) throw new Error("Token inválido");
                    return res.json();
                })
                .then(data => {
                    setUserData(data);    // Preenche dados do user
                    setIsLoggedIn(true);  // Marca login
                })
                .catch(err => {
                    //console.log(err);
                    localStorage.removeItem("token");
                });
        }
    }, []);
    useEffect(() => {
        const token = localStorage.getItem("token");
        fetch(`${BASE_URL}/invite/received`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`
            }
        }).then(response => {
            if (response.ok) return response.json();
        }).then(data => {
            setPendigContactList(data);
        }).catch(error => {
            console.log(error);
        })
    }, [isLoggedIn]);
    useEffect(() => {
        const token = localStorage.getItem("token");
        fetch(`${BASE_URL}/contacts`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`
            }
        }).then(response => {
            if (response.ok) return response.json();
        }).then(data => {
            setContactList(data);
        }).catch(error => {
            console.log(error);
        })
    }, [isLoggedIn]);
    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) return;

        fetch(`${BASE_URL}/contacts`, {
            method: "GET",
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(res => res.ok ? res.json() : [])
            .then(data => {
                setContactList(data);

                // buscar imagens com token
                data.forEach(contact => {
                    fetch(`${contact.profileImage}`, {
                        headers: { Authorization: `Bearer ${token}` }
                    })
                        .then(res => res.blob())
                        .then(blob => {
                            setContactPictures(prev => ({
                                ...prev,
                                [contact.id]: URL.createObjectURL(blob)
                            }));
                        })
                        .catch(err => console.log(err));
                });
            })
            .catch(err => console.log(err));
    }, [isLoggedIn]);
    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) return;

        fetch(`${BASE_URL}/availableChat`, {
            method: "GET",
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(res => res.ok ? res.json() : [])
            .then(data => {

                setChatList(data);
            })

    }, [isLoggedIn]); //meter aqui qualquer coisa para atualizar depois
    useEffect(() => {
        if (!isLoggedIn) return;

        const token = localStorage.getItem("token");
        //socketRef.current = new WebSocket(`ws://localhost:8080/ws/messages?userId=${userData.id}`);
        socketRef.current = new WebSocket(`${BASE_SOCKET_URL}/ws/messages?userId=${userData.id}`);

        socketRef.current.onopen = () => {
            console.log("Conectado ao WebSocket!");
        };
        socketRef.current.onmessage = (event) => {
            try {
                const msg = JSON.parse(event.data);
                if (msg.chatID === chatID) {
                    setMessages(prev => [...prev, msg]);
                } else {

                    setChatList(prev =>
                        prev.map(chat =>
                            chat.id === msg.chatID
                                ? { ...chat, unread: (chat.unread || 0) + 1 }
                                : chat
                        )
                    );
                }
            } catch (error) {
                console.error("Erro ao parsear mensagem:", error);
            }
        };
        // cleanup
        return () => {
            if (socketRef.current) socketRef.current.close();
        };
    }, [isLoggedIn, chatID]);
    useEffect(() => {
        if(!recipient) return;

        const user = contactList.find(item => item.id === recipient);

        if (user){
            setChatSelected(user);
            localStorage.setItem("user", JSON.stringify(user));
        }
    },[ contactList, recipient])

    return (
      <div className="App">
          <Login
              onLoginSuccess={handleLoginSuccess}
              isLoggedIn={isLoggedIn}
              isLoginMenuEnabled={isLoginMenuEnabled}
              isRegisterMenuEnabled={handleRegisterMenu}
          ></Login>
          <Register
              isRegisterMenuEnabled={isRegisterMenuEnabled}
              isRegister={handleRegisterMenu}
          ></Register>
          <User
              isPreferencesEnable={isPreferencesEnable}
              onPreferencesChange={handlePreferences}
              userData={userData}
              onLogout={handleLogout}
          ></User>
          <NewChat
              userData={userData}
              newChatMenu={newChatMenu}
              menuActionChat={handleNewChat}
              newContactMenu={handleNewContact}
              pendingContactList={pendingContactList}
              contactList={contactList}
              contactPictures={contactPictures}
          ></NewChat>
          <NewContact
              menuOpen={handleNewContact}
              userData={userData}
              newContact={newContact}
          ></NewContact>
          <div className="dashboard" hidden={isLoggedIn === false}>
              <div className="dashboard-container">
                  <Side
                      onPreferencesChange={handlePreferences}
                      sideContactsEnabled={handleSideContactsMenu}
                      contactPictures={contactPictures}
                      userData={userData}
                  ></Side>
                  <SideContacts
                      userData={userData}
                      newChat={handleNewChat}
                      pendingContactList={pendingContactList}
                      chatList={chatList}
                      contactPictures={contactPictures}
                      currentChatID={handleCurrentChatID}
                      sideContactsEnabled={sideContactsEnabled}
                  ></SideContacts>
                  <div className="msgbox-container">
                      {/*<div className="chat-container">*/}
                      <Header
                          chatSelected={chatSelected}
                          contactPictures={contactPictures}
                      ></Header>
                      <MsgBox
                          messages={messages}
                          userData={userData}
                          /*message={message}*/
                      ></MsgBox>
                      {/*</div>*/}
                      <TypeBox
                          onSendMessage={handleMessage}
                          userData={userData}
                      ></TypeBox>
                  </div>
              </div>
          </div>
      </div>
    );
}

export default App;
