function showMenu() {

    // when the page loads, change the class of navigation LI's
/*    const lis = document.getElementById("sectionsExamples").querySelectorAll("li");
    const l = lis.length;
    for (let i = 0; i < l; i++) {
        const anchor = lis[i].childNodes[0];
        let resource = anchor.getAttribute("resource");
        if (resource !== null) {
            const split = resource.split('/').pop().split('.');
            const imgname = split[0];
            if (imgname === "index" || imgname === "all") continue;
            const imgtype = split[1];
            if (imgtype === "js") continue;
            const span = document.createElement('span');
            span.className = "samplespan";
            const img = document.createElement('img');
            img.height = "100";
            img.src = "/images/screenshots/" + imgname + ".png";
            span.appendChild(img);
            anchor.appendChild(span);
        } else {
            lis[i].childNodes[0].remove();
        }
    }*/
}
