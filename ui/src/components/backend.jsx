import fetch from 'isomorphic-fetch';

export const props = {
    apiPrefix: 'http://localhost:8080/app'
};

export function get(resource, params = undefined) {
    return rest('GET', resource, params, undefined);
}

export function post(resource, body = {}, qs = undefined) {
    return rest('POST', resource, qs, body);
}

export function rest(method, url, queryString, body) {
    url = props.apiPrefix + url + renderQueryString(queryString);

    const headers = {};
    headers['Content-Type'] = 'application/json';
    body = JSON.stringify(body);

    return new Promise((resolve, reject) => {
        fetch(url, {
            method,
            body,
            headers,
            cache: 'no-cache',
            credentials: 'same-origin'
        })
            .then(confirmSuccessResponse)
            .then(parseResponse)
            .then(response => resolve(response))
            .catch(error =>   reject(error) );
    });
}

function renderQueryString(qs) {
    if (!qs) {
        return '';
    }

    let r = '';

    Object.keys(qs).forEach(key => {
        if (qs[key] == null) return;

        if (r !== '') r += '&';

        r += `${key}=${encodeURIComponent(qs[key])}`;
    });

    return `?${r}`;
}

async function confirmSuccessResponse(response) {
    if (response.status >= 200 && response.status < 300) {
        return response;
    }

    const text = await response.text();
    throw new Error(text || response.statusText);
}

async function parseResponse(response) {
    const contentType = response.headers.get('content-type');
    const text = await response.text();

    if (!contentType || contentType.indexOf('application/json') === -1) {
        return text;
    }

    return JSON.parse(text);
}