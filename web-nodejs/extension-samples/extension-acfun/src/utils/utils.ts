export default {
    emptyExtras: () => {
        return new Map<string, string>()
    },
    subLast: (str: string | undefined, symbol: string) => {
        const text = str || "";
        if (text.lastIndexOf(symbol) < 0) {
            return "";
        }

        return text.substring(text.lastIndexOf(symbol) + symbol.length, text.length);
    },
}

