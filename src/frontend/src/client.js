import fetch from 'unfetch';

// Proxy requests to backend 
const checkStatus = response => {
    if (response.ok) {
        return response;
    }
    // convert non-2xx HTTP responses into errors:
    const error = new Error(response.statusText);
    error.response = response;
    return Promise.reject(error);
}

export const getAllEmployees = () =>
    fetch("api/v1/employees")
        .then(checkStatus)
        .catch(err => alert(err));

export const addNewEmployee = employee => 
    fetch("api/v1/employees", {
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify(employee)
        }
    );
