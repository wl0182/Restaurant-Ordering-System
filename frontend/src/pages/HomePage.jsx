import React from 'react';
import { Link } from 'react-router-dom';
import './HomePage.css';




const HomePage = () => {
    return (

        <div className="home-container">
            <h1 className="home-heading" >Welcome</h1>
            <p className="home-subtext">Please select an option below:</p>
            <div className="home-links">
                    <Link to="/kitchen" className="home-button" >Kitchen Dashboard</Link>
                    <Link to="/server" className="home-button" >Server Dashboard</Link>
                    <Link to="/admin" className="home-button">Admin Dashboard</Link>
            </div>


        </div>
    );
};

export default HomePage;


