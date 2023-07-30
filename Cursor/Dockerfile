# copy /usr/local/bin/nebula-console from image: vesoft/nebula-console:v3

# Stage 1: Pull the vesoft/nebula-console:v3 image
FROM vesoft/nebula-console:v3 as nebula
# No need to do anything here, just pulling the image

# Stage 2: Pull the nicolarka/netshoot:lastest image and copy the file from the previous stage
FROM nicolarka/netshoot:lastest
COPY --from=nebula /usr/local/bin/nebula-console /usr/local/bin/nebula-console