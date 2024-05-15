declare global {
    interface ExtensionRequest {
        url: string;
        data?: any | undefined;
        method?: string | undefined;
        headers?: Map<string, string> | undefined;
    }

    interface ExtensionResponse {

    }

    interface MyGlobalObject {
        myField: string;

        myMethod(param: number): void;
    }

    const myGlobalVar: string;

    const request: (req: ExtensionRequest | undefined | null) => Promise<ExtensionResponse>;

    declare function getUrl(): string;
}

export {};
