import React from "react";
import {Button} from "reactstrap";
import SendMessageForm from "./SendMessageForm/SendMessageForm";
import UserService from "../../services/UserService";

export default class ManagerPlayground extends React.Component{

    constructor(props){
        super(props);

        this.state = {
            user: props.user,
            boardId: this.props.messageBoard ? this.props.messageBoard.id : undefined,
        };
        this.userService = new UserService();
    }


    componentWillMount(){

        let boardId = JSON.parse(localStorage.getItem("boardId"));
        if(boardId){
            this.setState({
                boardId: boardId
            })
        }
    }

    componentWillUnmount(){
        if(this.state.boardId !== undefined) {
            localStorage.setItem("boardId", JSON.stringify(this.state.boardId));
        }
    }

    createBoard(){
        // if(this.state.boardId !== undefined) {
            this.userService.createMessageBoard({
               email: this.state.user.email,
               playground: this.state.user.playground
            }).then(body => {
                this.setState({
                    boardId: body.elementId
                });
                console.log(JSON.stringify(body));
                if(this.state.boardId !== undefined) {
                    localStorage.setItem("boardId", JSON.stringify(this.state.boardId));
                }
            });
        // }

    }

    onSubmitMessage(title, content){
        this.userService.writeMessage({
            email: this.state.user.email,
            playground: this.state.user.playground
        }, {
            title: title,
            content: content
        },
            this.state.boardId
        );
    }

    render(){
        let messageBoardBtn = (<Button className={"messageBoardButton btn btn-info float-right"} onClick={this.createBoard.bind(this)}>
            Add Message Board
        </Button>);

        return(
            <div>
                {messageBoardBtn}
                <br/>
                <br/>
                <SendMessageForm onSubmit={this.onSubmitMessage.bind(this)}/>
            </div>
        );
    }
}