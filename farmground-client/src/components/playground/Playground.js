import React, { Component } from "react";
import './Playground.css'
import Game from './components/Game'
import {withRouter} from 'react-router-dom'
import MessageBoardPopOver from "./MessageBoard/MessageBoardPopOver";

import ManagerPlayground from "./ManagerPlayground";

class Playground extends Component {

    constructor(props){
        super();

        this.state = {
            user: props.location.state !== undefined ? props.location.state.user : undefined,
            ok: false,
            plants: props.location.state.elements.filter(element => element.type.toString().toLowerCase() === 'plant'),
            messageBoard: props.location.state !== undefined ? this.getMessageBoardIfExists(props.location.state.elements) : undefined,
            props: props
        };

    }

    getMessageBoardIfExists(elements){
        let messagesBoards = elements.filter(element => element.type.toString().toLowerCase() === 'messagesboard');

        if(messagesBoards.length > 0){
            return messagesBoards[0];
        }

        return undefined;
    }

    componentDidMount(){
        if(this.props.location.state){
            localStorage.setItem("user", JSON.stringify(this.props.location.state.user));
        }else{
            this.setState({
                user: JSON.parse(localStorage.getItem("user"))
            });
        }


    }

    render() {
        let date = new Date();
        console.log(date + " " + date.getMilliseconds() + " In Playground user:" + JSON.stringify(this.state.user));
        console.log(date + " " + date.getMilliseconds() +" In Playground plants:" + JSON.stringify(this.state.plants));

        let content = <br/>;

        let messageBoardPopOver = <br/>;


        if(this.state.messageBoard) {
            messageBoardPopOver = <MessageBoardPopOver id={"messageBoard"} user={this.state.user}
                                                       messageBoard={this.state.messageBoard}/>;
        }

        if(this.state.user !== undefined && this.state.user.role.toString().toLowerCase() === "player"){
            content = <Game user={this.state.user} plants={this.state.plants}/>;
        }else if(this.state.user !== undefined && this.state.user.role.toString().toLowerCase() === "manager"){
            content = <ManagerPlayground user={this.state.user} messageBoard={this.state.messageBoard} messageBoardComponent={messageBoardPopOver}/>;
        }

        let userHeader = this.state.user ? <h2> {this.state.user.avatar} {this.state.user.username}</h2> : <br/>;

        return (
          <div id={"playgroundSC"} className={"container"}>
              {messageBoardPopOver}
              {userHeader}
              {content}
          </div>
        );
    }
}

export default withRouter(Playground);