import React from 'react';
import Board from './Board';
import './style.scss';

export default class Game extends React.Component {

    constructor(props){
        super(props);
        this.props = props;
        this.state = {
            height: 6,
            width: 10,
            farmPoints: this.props.user ? this.props.user.points : 0,
            user: this.props.user,
            plants: this.props.plants
        };
        console.log("In Game plants:" + JSON.stringify(this.state.plants));

    }

    render() {
        const { height, width, farmPoints, user, plants } = this.state;
        return (
            <div className="game">
                <Board height={height} width={width} points={farmPoints} plants={plants} user={user}/>
            </div>
        );
    }
}
