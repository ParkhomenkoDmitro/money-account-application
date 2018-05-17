import React from 'react';

import {Card, CardContent, LinearProgress, Typography} from "material-ui";
import {get} from "./backend";

class OrganizationView extends React.Component {

    state = {
        organization: {}
    };

    componentDidMount() {
        get('/transactions/organization')
            .then(data => this.setState({ organization: data }));
    }

    render() {
        return (
            <div>
                <Card>
                    <CardContent>
                        { Object.keys(this.state.organization).length === 0 && <LinearProgress /> }

                        <Typography variant="headline">Organization name: { this.state.organization.name }</Typography>
                        <Typography variant="headline">Organization balance: { this.state.organization.amount } $</Typography>
                    </CardContent>
                </Card>
            </div>
        );
    }
}

export default OrganizationView;