import React from 'react';
import PropTypes from 'prop-types';
import Cell from './Cell';
import UserService from "../../../services/UserService";

export default class Board extends React.Component {


    constructor(props){
        super(props);
        let plants = this.props.plants;
        let points = this.props.points;
        let user = props.user ? props.user : null;
        let userService = new UserService((res) => {

        }, this.onPlant.bind(this));

        console.log("CONSTRUCTOR:: plants - " + JSON.stringify(plants));
        this.state = {
            plants: plants,
            boardData: Board.initBoardData(this.props.height, this.props.width, plants),
            farmPoints: points,
            userService: userService,
            user: user
        };

    }
    // Gets initial board data
    static initBoardData(height, width, plants) {
        let data = Board.createEmptyArray(height, width);
        data = Board.putPlants(data, height, width, plants);
        return data;
    }


    static createEmptyArray(height, width) {
        let data = [];

        for (let i = 0; i < height; i++) {
            data.push([]);
            for (let j = 0; j < width; j++) {
                data[i][j] = {
                    x: i,
                    y: j,
                    isPlanted: false,
                    neighbour: 0,
                    isHarvested: false,
                    isEmpty: false,
                    isFlagged: false,
                    emoji: ""
                };
            }
        }


        return data;
    }

    // plant farmPoints on the board
    static putPlants(data, height, width, plants) {

        let updatedData = data;


        console.log("*** INIT PLANTS BEFORE LOOP: " + JSON.stringify(plants));

        for(let plant of plants){
            let x = plant.location.x;
            let y = plant.location.y;
            let emoji = plant.attributes.COLOR;

            updatedData[x][y].isPlanted = true;
            updatedData[x][y].emoji = emoji;
            updatedData[x][y].x = x;
            updatedData[x][y].y = y;
        }

        return updatedData;
    }

    onPlant(plantInfo){

        let x = plantInfo.attributes.X;
        let y = plantInfo.attributes.Y;
        let points = plantInfo.attributes.points;

        let updatedData = this.state.boardData;
        updatedData[x][y].isPlanted = true;
        updatedData[x][y].emoji = plantInfo.attributes.COLOR;
        updatedData[x][y].x = x;
        updatedData[x][y].y = y;
        //TODO -> ADD EMOJI ATTRIBUTE
        this.setState({
            boardData: updatedData,
            farmPoints: points
        });
    }

    // Handle User Events
    /** left cursor action **/
    handleCellClick(x, y) {

        // check if revealed. return if true.
        if (this.state.boardData[x][y].isHarvested || this.state.boardData[x][y].isFlagged)
            return null;
//["🥭", "Mango"], ["🥬", "Lettuce"],
        let emojis = [["🍆", "eggplant"], ["🍅", "Tomato"],
            ["🌶", "Red hot chili pepper"], ["🌽", "Corn"],
             ["🍓", "Strawberry"], ["🍒", "Cherry"], ["🍎", "Apple"],
            ["🍊","Orange"], ["🍐", "Pear"],
             ["🥕", "Carrot"]
            , ["🍇", "Grapes"], ["🍑", "Peach"], ["🥔", "Potato"]];

        var randEmoji = emojis[Math.floor(Math.random() * emojis.length)];


        if(!this.state.boardData[x][y].isPlanted) {

            let userService = this.state.userService;

            userService.plant(this.state.user, randEmoji[0], randEmoji[1], x, y);
        }
    }


    /** right cursor action **/
    handleContextMenu(e, x, y) {
        e.preventDefault();
        console.log(e + "$x $y", x, y)
    }

    renderBoard(data) {
        return data.map((datarow) => {
            return datarow.map((dataitem) => {
                return (
                    <div id={dataitem.x * datarow.length + dataitem.y} key={dataitem.x * datarow.length + dataitem.y}>
                        <Cell
                            onClick={() => this.handleCellClick(dataitem.x, dataitem.y)}
                            cMenu={(e) => this.handleContextMenu(e, dataitem.x, dataitem.y)}
                            value={dataitem}
                        />
                        {(datarow[datarow.length - 1] === dataitem) ? <div className="clear" /> : ""}
                    </div>);
            })
        });

    }

    render() {
        return (
            <div className="board">
                <div className="game-info">
                    <span className="info">Points: {this.state.farmPoints}</span>
                    <h1 id={"title"}>
                    <span className="info1">{"Farm"}</span>
                    <span className="info2">{"Ground"}</span>
                    </h1>
                </div>
                <div className={"actualLands"}>
                    {
                        this.renderBoard(this.state.boardData)
                    }
                </div>
            </div>
        );
    }
}

Board.propTypes = {
    height: PropTypes.number,
    width: PropTypes.number,
    mines: PropTypes.number,
};