# hassq

Very early, dumb and naive implementation of a cli for Home Assistant using Quarkus.

Do NOT use this for anything serious and do NOT expect it to work nor is the code/structure
anywhere near final.


## Basic usage examples

List state with entity id contains "kitchen"

```
export HASS_TOKEN=your-token
hassq state list kitchen
```


## Background

Around 2018/2019 I started to use Home Assistant and I really liked it. I also started to
move forward the hass-cli python project (https://github.com/home-assistant/home-assistant-cli) to 
the extent it had several releases and became really useful for me. You can read more about it [here](https://www.home-assistant.io/blog/2019/02/04/introducing-home-assistant-cli/) and even got on the podcast [here](https://www.youtube.com/watch?v=m8AQrlfwfCg).

However, I went on a sabbatical and got to work on other things. I also got back to using Java and
just didn't have the time to work on the hass-cli project. 

Now some years later I'm back to using Home Assistant and hass-cli recently haven't had working releases
and xmas 2024 I really wanted to have a working cli for Home Assistant. So I decided to start a new Java
based on using Quarkus.

Thus this is not trying to displace hass-cli but rather be a new alternative. 

Not sure where it will go but at least I have a working cli for my own use now and I have a way to test Quarkus developer experience.

The advantages of using Java is that its dependencies are much easier to manage and does not conflict with other Python dependencies. Meaning that even 5 years from now this code shuold be able to run without any issues - assuming HomeAssistant API is still the same. 
