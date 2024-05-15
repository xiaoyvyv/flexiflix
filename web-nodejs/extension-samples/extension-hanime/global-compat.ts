/*
function createPromise(action: (resolve: (value: any) => void, reject: (reason?: any) => void) => void): Promise<any> {
    return new Promise((resolve, reject) => {
        action(resolve, reject)
    });
}

const obj = {
    action: function (resolve: (value: any) => void, reject: (reason?: any) => void) {
    }
}

async function f() {
    await createPromise(obj.action);
}*/
