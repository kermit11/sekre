
function addLinkTo(pageStart, pageSize, elem)
{
    var link = document.createElement("a");
    link.setAttribute('href', referTo+'?pageStart='+pageStart+'&pageSize='+pageSize);
    link.innerHTML += elem.innerHTML;
    elem.innerHTML = "";
    elem.appendChild(link);
}

function leftArrowSection()
{
    var td = document.createElement("td");
    td.innerHTML += '&lArr;';

    return td;
}

function rightArrowSection()
{
    var td = document.createElement("td");
    td.innerHTML = '&rArr;';

    return td;
}

function firstPageSection(pageSize, total)
{
    var pageStart = Math.min(1, total);
    var pageEnd = Math.min(pageSize, total);
    var td = document.createElement("td");
    td.innerHTML = pageStart + ' - ' + pageEnd;
    return td;
}

function secondPageSection(pageSize, total)
{
    var pageStart = pageSize+1;
    var pageEnd = Math.min(2*pageSize, total);
    var td = document.createElement("td");
    td.innerHTML = pageStart + ' - ' + pageEnd;
    return td;
}

function thirdPageSection(pageSize, total)
{
    var pageStart = 2*pageSize+1;
    var pageEnd = Math.min(3*pageSize, total);
    var td = document.createElement("td");
    td.innerHTML = pageStart + ' - ' + pageEnd;
    return td;
}

function lastPageSection(pageSize, total)
{
    var pageStart = Math.floor((total-1) / pageSize) * pageSize + 1;
    var td = document.createElement("td");
    td.innerHTML = pageStart + ' - ' + total;
    return td;
}

function ellipsisSection()
{
    var td = document.createElement("td");
    td.innerHTML = '...';
    return td;
}

function exactPageSection(pageStart, pageSize, curStart, total)
{
    var pageEnd = Math.min(pageStart+pageSize-1, total);
    var td = document.createElement("td");
    td.innerHTML = pageStart + ' - ' + pageEnd;
    return td;
}



//Main function
function createPaginationElement(pageSize, pageStart, totalSize, pagTR)
{
    var td;

    //Left arrow
    if(pageStart > pageSize)
    {
        td = leftArrowSection();
        addLinkTo(pageStart-pageSize, pageSize, td);
        pagTR.appendChild(td);
    }

    //First page
    td = firstPageSection(pageSize,totalSize);
    if(pageStart > pageSize)
    {
        addLinkTo(1, pageSize, td);
    }
    pagTR.appendChild(td);

    //Starting after 3 or more pages, show an ellipsis after the first section
    if (pageStart > 3*pageSize)
    {
        pagTR.appendChild(ellipsisSection());

        //Previous page
        td = exactPageSection(pageStart-pageSize, pageSize, pageStart, totalSize);
        addLinkTo(pageStart-pageSize, pageSize, td);
        pagTR.appendChild(td);

        //Current page
        pagTR.appendChild(exactPageSection(pageStart, pageSize, pageStart, totalSize));
    }
    //Starting at 2nd page
    else if (pageStart > pageSize && pageStart <= pageSize*2)
    {
        pagTR.appendChild(secondPageSection(pageSize,totalSize));
    }
    //Starting at 3rd page
    else if (pageStart > 2*pageSize)
    {
        //Second page
        td = secondPageSection(pageSize,totalSize);
        addLinkTo(pageSize+1, pageSize, td);
        pagTR.appendChild(td);

        //Third page
        pagTR.appendChild(thirdPageSection(pageSize,totalSize));
    }

    //At least one more page to the end
    if (totalSize >= pageStart + pageSize)
    {
        td = exactPageSection(pageStart + pageSize, pageSize, pageStart, totalSize);
        addLinkTo(pageStart + pageSize, pageSize, td);
        pagTR.appendChild(td);
    }

    //Two or more pages to the end
    if (totalSize >= pageStart + pageSize*2)
    {
        //Three or more pages to the end, show an ellipsis before the last one
        if (totalSize >= pageStart + pageSize*3)
        {
            pagTR.appendChild(ellipsisSection());
        }
        td = lastPageSection(pageSize, totalSize);
        var lastPageStart = Math.floor((totalSize-1) / pageSize) * pageSize + 1;
        addLinkTo(lastPageStart, pageSize, td);
        pagTR.appendChild(td);
    }

    if(pageStart <= totalSize - pageSize)
    {
        td = rightArrowSection();
        addLinkTo(pageStart+pageSize, pageSize, td);
        pagTR.appendChild(td);
    }
}