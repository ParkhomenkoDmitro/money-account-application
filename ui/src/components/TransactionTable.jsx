import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from 'material-ui/styles';
import Table from 'material-ui/Table';
import {
    LinearProgress,
    Paper,
    TableBody,
    TableCell,
    TableHead,
    TablePagination,
    TableRow
} from 'material-ui';
import {get} from './backend';

const columnData = [
    { id: 'id', numeric: true, disablePadding: false, label: 'Id' },
    { id: 'creation', numeric: false, disablePadding: false, label: 'Creation time' },
    { id: 'type', numeric: false, disablePadding: false, label: 'Type' },
    { id: 'amount', numeric: true, disablePadding: false, label: 'Money ($)' }
];

const styles = theme => ({
    table: {
        minWidth: 700,
    },
    tableWrapper: {
        overflowX: 'auto',
    },
});

class EnhancedTableHead extends React.Component {
    render() {
        return (
            <TableHead>
                <TableRow>
                    {columnData.map(column => {
                        return (
                            <TableCell
                                key={column.id}
                                numeric={column.numeric}
                                padding={column.disablePadding ? 'none' : 'default'}
                            >
                                {column.label}
                            </TableCell>
                        );
                    }, this)}
                </TableRow>
            </TableHead>
        );
    }
}

class TransactionHistoryGrid extends React.Component {
    constructor(props, context) {
        super(props, context);

        this.state = {
            total: 0,
            data: [],
            page: 0,
            rowsPerPage: 5,
            showLoader: false
        };
    }

    componentDidMount() {
        this.setState({ showLoader: true });

        get('/transactions', { page: this.state.page, size: this.state.rowsPerPage })
            .then(data => this.setState({ data: data.rows, total: data.total.count, showLoader: false }))
            .catch(e => this.setState({ showLoader: false }));
    }

    handleChangePage = (event, page) => {
        this.setState({ showLoader: true });

        get('/transactions', { page: page, size: this.state.rowsPerPage })
            .then(data => this.setState({ page: page, data: data.rows, total: data.total.count, showLoader: false }))
            .catch(e => this.setState({ showLoader: false }));
    };

    handleChangeRowsPerPage = event => {
        const rowsPerPage = event.target.value;
        this.setState({ showLoader: true });

        get('/transactions', { page: this.state.page, size: rowsPerPage })
            .then(data => this.setState({ rowsPerPage: rowsPerPage, data: data.rows, total: data.total.count, showLoader: false }))
            .catch(e => this.setState({ showLoader: false }));
    };

    render() {
        const { classes } = this.props;
        const { data, total, rowsPerPage, page } = this.state;
        const emptyRows = rowsPerPage - Math.min(rowsPerPage, total - page * rowsPerPage);

        return (
            <Paper>
                <div className={classes.tableWrapper}>
                    { this.state.showLoader && <LinearProgress /> }

                    <Table className={classes.table} aria-labelledby="tableTitle">
                        <EnhancedTableHead/>
                        <TableBody>
                            {data.map(n => {
                                return (
                                    <TableRow style = {{ backgroundColor: n.isCredit ? '#FFF8DC' : '#B8DC6F' }}
                                        hover
                                        key={n.id}
                                    >
                                        <TableCell numeric>{n.id}</TableCell>
                                        <TableCell>{n.creation}</TableCell>
                                        <TableCell>{n.isCredit ? 'Credit' : 'Debit'}</TableCell>
                                        <TableCell numeric>{n.amount}</TableCell>
                                    </TableRow>
                                );
                            })}

                            {emptyRows > 0 && (
                                <TableRow style={{ height: 49 * emptyRows }}>
                                    <TableCell colSpan={6} />
                                </TableRow>
                            )}

                        </TableBody>
                    </Table>
                </div>

                <TablePagination
                    component="div"
                    count={total}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    backIconButtonProps={{
                        'aria-label': 'Previous Page',
                    }}
                    nextIconButtonProps={{
                        'aria-label': 'Next Page',
                    }}
                    onChangePage={this.handleChangePage}
                    onChangeRowsPerPage={this.handleChangeRowsPerPage}
                />
            </Paper>
        );
    }
}

TransactionHistoryGrid.propTypes = {
    classes: PropTypes.object.isRequired
};

export default withStyles(styles)(TransactionHistoryGrid);

