# Dockerfile for React frontend (Vite)
FROM node:20-alpine AS build
WORKDIR /app
# Copy package-lock.json only if it exists
COPY package.json ./
COPY package-lock.json ./
RUN npm ci || npm install
COPY . .
RUN npm run build

# Serve static files with nginx
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
