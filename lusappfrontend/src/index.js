import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import reportWebVitals from './reportWebVitals';

import './assets/styles/index.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './assets/styles/login.css';
import './assets/styles/side.css';
import './assets/styles/sideContacts.css';
import './assets/styles/header.css';
import './assets/styles/msgbox.css';
import './assets/styles/typebox.css';
import './assets/styles/user.css';
import './assets/styles/register.css';
import './assets/styles/newchat.css';
import './assets/styles/newcontact.css';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
