export const RequestMethod = {
    GET: 'get',
    POST: 'post',
    PUT: 'put',
    DELETE: 'delete'
};

const defaultSuccess = function (object) {
    // console.log("Request passed, returned object:");
    // console.log(object);
}

const defaultFailure = function(code, response) {
    // console.log("Request failed: Return code = " + code + ", response = " + response);
}

export default class Request {
    constructor(address, successCallback = defaultSuccess, failureCallback = defaultFailure) {
        this.address = address;

        this.xhr = new XMLHttpRequest();
        this.xhr.onreadystatechange = () => {
            if (this.xhr.readyState === 4) {
                if (this.xhr.status === 200) {
                    if (Object.keys(this.xhr.responseText).length === 0) {
                        successCallback(null);
                    } else {
                        successCallback(JSON.parse(this.xhr.responseText));
                    }
                } else {
                    failureCallback(this.xhr.status, this.xhr.responseText);
                }
            }
        }
    }

    send(method, endPoint, object) {
        if (!method)
            throw "Request.send: Method is undefined";

        if (method === RequestMethod.GET || method === RequestMethod.DELETE) {
            this.xhr.open(method, this.address + endPoint, true);
            this.xhr.send();
        } else if (method === RequestMethod.POST || method === RequestMethod.PUT) {
            this.xhr.open(method, this.address + endPoint, true);
            this.xhr.setRequestHeader("Accept", "application/json");
            this.xhr.setRequestHeader("Content-type", "application/json");

            if (object)
                this.xhr.send(object);
            else
                this.xhr.send();
        } else {
            throw "Request.send: Method is unknown";
        }
    }

}