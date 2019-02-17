
import React from 'react';
import { Button, Popover, PopoverHeader, PopoverBody } from 'reactstrap';
import MessageBoard from "./MessageBoard";
import UserService from "../../../services/UserService";

class MessageBoardPopOver extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            user: this.props.user,
            messageBoard: this.props.messageBoard,
            messages: [],
            popoverOpen: false,
            placement: 'top',
            text: '📥 Message Board'

        };
    }

    toggle() {
        this.setState({
            popoverOpen: !this.state.popoverOpen
        });
    }

    refreshMessages(event){
        event.preventDefault();

        let userService = new UserService();
        let messageBoard = this.state.messageBoard;
        userService.readMessages({
            email: this.state.user.email,
            playground: this.state.user.playground
        }, messageBoard.id).then(body => {
            this.setState({
                messages: body.attributes.MESSAGES
            });
        });
    }

    render() {
        return (
            <div className={"float-right"}>
                <span>
                    <Button className="messageBoardButton mr-1" color="success" id={'Popover-MessageBoard'} type="button" onClick={this.refreshMessages.bind(this)}>
                        {this.state.text}
                    </Button>
                    <Popover placement={this.state.placement} isOpen={this.state.popoverOpen} target={'Popover-MessageBoard'} toggle={this.toggle.bind(this)}>
                        <PopoverHeader>
                            <span role="img" aria-label={"Message logo"}>📩</span> Message Board
                        </PopoverHeader>
                        <PopoverBody>
                            <MessageBoard messages={this.state.messages}/>
                        </PopoverBody>
                    </Popover>
                </span>
            </div>
        );
    }
}

export default MessageBoardPopOver;
