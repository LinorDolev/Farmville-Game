import React from 'react';
import PropTypes from 'prop-types';

export default class Cell extends React.Component {
    getValue() {
        const { value } = this.props;

        return value.emoji;
    }

    render() {
        const { value, onClick, cMenu } = this.props;
        let className =
            "cell" +
            (value.isHarvested ? "" : " hidden") +
            (value.isPlanted ? " is-mine" : " is-mine") +
            (value.isFlagged ? " is-flag" : "");

        return (
            <div
                onClick={onClick}
                className={className}
                onContextMenu={cMenu}
            >
                {this.getValue()}
            </div>
        );
    }
}

const cellItemShape = {
    isHarvested: PropTypes.bool,
    isPlanted: PropTypes.bool,
    isFlagged: PropTypes.bool
}

Cell.propTypes = {
    value: PropTypes.objectOf(PropTypes.shape(cellItemShape)),
    onClick: PropTypes.func,
    cMenu: PropTypes.func
}
