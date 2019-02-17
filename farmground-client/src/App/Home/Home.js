import React, { Component } from "react";
import "./Home.css";

export default class Home extends Component {
    render() {
        return (
            <div className="Home">
                <div className="lander">
                    <h1>
                        <span className={"info1"}>Farm</span>
                        <span className={"info2"}>Ground</span>
                    </h1>
                    <p>The fascinating experience of being a farmer!</p>
                </div>
            </div>
        );
    }
}