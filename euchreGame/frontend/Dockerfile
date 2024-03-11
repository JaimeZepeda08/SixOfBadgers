FROM node:18-alpine
WORKDIR /app
COPY package.json .
COPY package-lock.json .
COPY . . 
RUN npm i 
EXPOSE 3000
ENTRYPOINT [ "npm", "run", "dev" ]