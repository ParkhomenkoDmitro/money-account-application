import React from 'react';
import NumberFormat from 'react-number-format';
import {
    Button,
    Card,
    CardContent,
    FormControl,
    FormControlLabel,
    FormLabel, IconButton,
    Radio,
    RadioGroup, Snackbar,
    TextField
} from 'material-ui';
import PropTypes from 'prop-types';
import { withStyles } from 'material-ui/styles';
import {get, post} from './backend';

const ERROR_MAPPING_LOCALIZATION = {
    ZERO_AMOUNT_ERROR: 'Fail: transaction amount is zero!',
    NEGATIVE_AMOUNT_SYSTEM_ERROR: 'Fail: the amount of money on the account is not enough to process this transaction',
    INVALID_ORGANIZATION_ERROR: 'Fail: organization from your transaction is not exist',
    VERY_BIG_DEBET_ERROR: 'Fail: debit transaction amount is too big, try less amount'
};

const styles = theme => ({
    container: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    formControl: {
        margin: theme.spacing.unit
    }
});

function NumberFormatCustom(props) {
    const { inputRef, onChange, ...other } = props;

    return (
        <NumberFormat
            {...other}
            ref={inputRef}
            onValueChange={values => {
                onChange({
                    target: {
                        value: values.value,
                    }
                });
            }}
            thousandSeparator
            prefix="$"
        />
    );
}

class AddTransaction extends React.Component {

    state = {
        organization: {},
        amount: '0',
        transactionType: 'credit',
        showErrorSnackBar: false,
        messageSnackBar: ''
    };

    componentDidMount() {
        get('/transactions/organization')
            .then(data => this.setState({ organization: data }));
    }

    handleChange = name => event => {
        this.setState({
            [name]: event.target.value,
        });
    };

    save = event => {
        post('/transactions', {
            amount: this.state.amount,
            isCredit: this.state.transactionType === 'credit',
            organizationId: this.state.organization.id
        })
            .then(res => this.setState({
                showErrorSnackBar: true,
                messageSnackBar: 'Transaction committed successfully' }))
            .catch(e => {
                const data = JSON.parse(e.message);
                const errorCode = data.errorCode;

                this.setState({
                    showErrorSnackBar: true,
                    messageSnackBar: ERROR_MAPPING_LOCALIZATION[errorCode] });
            });
    };

    handleCloseSnackBar = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }

        this.setState({ showErrorSnackBar: false, messageSnackBar: '' });
    };

    render() {
        const { classes } = this.props;

        return (
            <div>
                <Card>
                    <CardContent>

                        <FormControl component="fieldset" required className={ classes.formControl }>
                            <FormLabel component="legend">Transaction type</FormLabel>
                            <RadioGroup
                                aria-label="gender"
                                name="gender1"
                                className={ classes.group }
                                value={ this.state.transactionType }
                                onChange={ this.handleChange('transactionType') }
                            >
                                <FormControlLabel value="credit" control={ <Radio /> } label="Credit" />
                                <FormControlLabel value="debit" control={ <Radio /> } label="Debit" />
                            </RadioGroup>
                        </FormControl>

                        <TextField
                            className={ classes.formControl }
                            label="Amount"
                            value={ this.state.amount }
                            onChange={ this.handleChange('amount') }
                            id="formatted-numberformat-input"
                            InputProps={{
                                inputComponent: NumberFormatCustom,
                            }}
                        />

                        <Button variant="raised" color="primary" className={ classes.button }
                                onClick={ this.save }>
                            Save
                        </Button>

                        <Snackbar
                            anchorOrigin={{
                                vertical: 'top',
                                horizontal: 'center',
                            }}
                            open={ this.state.showErrorSnackBar }
                            autoHideDuration={ 6000 }
                            onClose={ this.handleCloseSnackBar }
                            ContentProps={{
                                'aria-describedby': 'message-id',
                            }}
                            message={ <span id="message-id">{ this.state.messageSnackBar }</span> }
                            action={[
                                <IconButton
                                    key="close"
                                    aria-label="Close"
                                    color="inherit"
                                    className={ classes.close }
                                    onClick={ this.handleCloseSnackBar }
                                >
                                   X
                                </IconButton>,
                            ]}
                        />

                    </CardContent>
                </Card>
            </div>
        );
    }
}

AddTransaction.propTypes = {
    classes: PropTypes.object.isRequired
};

export default withStyles(styles)(AddTransaction);