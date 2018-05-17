import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from 'material-ui/styles';
import AppBar from 'material-ui/AppBar';
import Tabs, { Tab } from 'material-ui/Tabs';
import Typography from 'material-ui/Typography';
import OrganizationView from './OrganizationView';
import AddTransaction from './AddTransaction';
import TransactionTable from './TransactionTable';
import { Link } from 'react-router-dom';
import { Switch, Route } from 'react-router-dom'

function TabContainer(props) {
    return (
        <Typography component="div" style={{ padding: 8 * 3 }}>
            { props.children }
        </Typography>
    );
}

TabContainer.propTypes = {
    children: PropTypes.node.isRequired
};

const styles = theme => ({
    root: {
        flexGrow: 1,
        backgroundColor: theme.palette.background.paper
    },
    tabLink : {
        display:"flex",
        alignItems:"center",
        justifyContent:"center"
    }
});

const URL = {
    organization_view: '/',
    add_transaction: '/add-transaction',
    list_transactions: '/transactions'
};

const TABS_INFO = [
    { path: URL.organization_view, label: 'Organization view'},
    { path: URL.add_transaction, label: 'Add transaction'},
    { path: URL.list_transactions, label: 'Transactions history'}
];

function setActiveTab() {
    const href = window.location.href;

    if(href.indexOf(URL.add_transaction) !== -1) {
        return 1;
    }

    if(href.indexOf(URL.list_transactions) !== -1) {
        return 2;
    }

    return 0;
}

class App extends React.Component {
    state = {
        value: setActiveTab()
    };


    handleChange = (event, value) => {
        this.setState({ value });
    };

    render() {
        const { classes } = this.props;
        const { value } = this.state;

        return (
            <div className={ classes.root }>
                <AppBar position="static" color="primary">
                    <Tabs value={ value } onChange={ this.handleChange } fullWidth>
                        {
                            TABS_INFO.map(
                                ({ path, label })=><Tab key={ label }
                                                      label={ label }
                                                      className={ classes.tabLink }
                                                      component={ Link }
                                                      to={ path } />
                            )
                        }
                    </Tabs>
                </AppBar>

                <TabContainer>
                    <Switch>
                        <Route exact path={ URL.organization_view } component={ OrganizationView }/>
                        <Route path={ URL.add_transaction } component={ AddTransaction }/>
                        <Route path={ URL.list_transactions } component={ TransactionTable }/>
                    </Switch>
                </TabContainer>

            </div>
        );
    }
}

App.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(App);