FROM node:18-alpine
COPY . /app
COPY package.json ./
COPY package-lock.json ./
WORKDIR /app
RUN npm i 
EXPOSE 3000
ENTRYPOINT [ "npm", "run", "dev" ]